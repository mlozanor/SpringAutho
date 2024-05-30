'use strict';

angular.module('myApp.dashboard', ['ngRoute'])
.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/', {
    templateUrl: 'dashboard/dashboard.html',
    controller: DashboardCtrl,
    resolve: DashboardCtrl.resolve
  });
}]);

function DashboardCtrl($scope, $rootScope, $http, isAuthenticated, authService) {
  $rootScope.authenticated = isAuthenticated;

  $scope.serverResponse = '';
  $scope.responseBoxClass = '';

  var setResponse = function(res, success) {
    $rootScope.authenticated = isAuthenticated;
    if (success) {
      $scope.responseBoxClass = 'alert-success';
    } else {
      $scope.responseBoxClass = 'alert-danger';
    }
    $scope.serverResponse = res;
    $scope.serverResponse.data = JSON.stringify(res.data, null, 2);
  }

  if ($rootScope.authenticated) {
    authService.getUser()
    .then(function(response) {
      $scope.user = response.data;
    });
  }

  $scope.getUserInfo = function() {
    authService.getUser()
    .then(function(response) {
      setResponse(response, true);
    })
    .catch(function(response) {
      setResponse(response, false);
    });
  }

  $scope.getAllUserInfo = function() {
    if ($scope.user.authorities.length === 1) {
        alert('USTED NO ESTA AUTORIZADO PARA ACCEDER ESTA INFORMACION. SE NOTIFICARA AL ADMINISTRADOR'); 
    }
    else if ($scope.user.authorities.length === 2) {
        alert('Cargando todos los Usuarios...'); 
    }

    $http({
        headers: authService.createAuthorizationTokenHeader(),
        method: 'GET',
        url: 'api/user/all'
    })
    .then(function(res) {
        setResponse(res, true);
    })
    .catch(function(response) {
        setResponse(response, false);
    });
}

  $scope.getSolicitudInfo = function() {
    $http({
      headers: authService.createAuthorizationTokenHeader(),
      method: 'GET',
      url: 'api/solicitudPropia'
    })
    .then(function(res) {
      setResponse(res, true);
    })
    .catch(function(response) {
      setResponse(response, false);
    });
  }

  $scope.getAllSolicitudInfo = function() {
    if ($scope.user.authorities.length === 1) {
        alert('USTED NO ESTA AUTORIZADO PARA ACCEDER ESTA INFORMACION. SE NOTIFICARA AL ADMINISTRADOR'); 
    }
    else if ($scope.user.authorities.length === 2) {
        alert('Cargando todas las solicitudes...'); 
    }

    $http({
        headers: authService.createAuthorizationTokenHeader(),
        method: 'GET',
        url: 'api/solicitud/all'
    })
    .then(function(res) {
        setResponse(res, true);
    })
    .catch(function(response) {
        setResponse(response, false);
    });
}

$scope.showCreateSolicitudForm = function() {
  $scope.showCreateSolicitud = true;
};

$scope.hideCreateSolicitudForm = function() {
  $scope.showCreateSolicitud = false;
};

$scope.createSolicitud = function(tipo, fecha) {
  // Construir la cadena de inserción SQL
  var sqlInsert = "INSERT INTO SOLICITUDES (tipo, fecha) VALUES ('" + tipo + "', '" + fecha + "');\n";

  // Enviar los datos del formulario al backend
  $http({
      method: 'POST',
      url: '/api/solicitud/create',
      headers: {
          'Content-Type': 'application/json'
      },
      data: {
          tipo: tipo,
          fecha: fecha
      }
  })
  .then(function(response) {
      // Escribir la cadena de inserción SQL en el archivo import.sql
      $http({
          method: 'POST',
          url: '/api/writeSql',
          headers: {
              'Content-Type': 'text/plain'
          },
          data: sqlInsert
      })
      .then(function(response) {
          console.log('Datos guardados en import.sql');
      })
      .catch(function(error) {
          console.error('Error al escribir en el archivo:', error);
      });

      $scope.hideCreateSolicitudForm(); // Ocultar el formulario después de crear la solicitud
  })
  .catch(function(error) {
      console.error('Error al crear la solicitud:', error);
  });
};

}

DashboardCtrl.resolve = {
  isAuthenticated: function($q, $http, AuthService) {
    var deferred = $q.defer();
    var oldToken = AuthService.getJwtToken();
    if (oldToken !== null) {
      $http({
        headers: AuthService.createAuthorizationTokenHeader(),
        method: 'POST',
        url: 'auth/refresh'
      })
      .success(function(res) {
        AuthService.setJwtToken(res.access_token);
        deferred.resolve(res.access_token !== null);
      })
      .error(function(err) {
        AuthService.removeJwtToken();
        deferred.resolve(false);
      });
    } else {
      deferred.resolve(false);
    }
    return deferred.promise;
  }
};

DashboardCtrl.$inject = ['$scope', '$rootScope', '$http', 'isAuthenticated', 'AuthService'];
