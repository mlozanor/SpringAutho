package com.bfwg.rest;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bfwg.model.Solicitud;
import com.bfwg.service.SolicitudService;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class SolicitudController {

    private SolicitudService solicitudService;
    private int nextId = 18; // Inicializar el próximo ID en 18

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @GetMapping("/solicitud/{solicitudId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Solicitud loadById(@PathVariable Long solicitudId) {
        return this.solicitudService.findById(solicitudId);
    }

    @GetMapping("/solicitud/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Solicitud> loadAll() {
        return this.solicitudService.findAll();
    }

    @PostMapping("/solicitud/create")
@PreAuthorize("hasRole('USER')")
public Solicitud createSolicitud(@RequestParam String tipo, @RequestParam String fecha) {
    // Crear la solicitud
    Solicitud solicitud = new Solicitud();
    solicitud.setTipo(tipo);
    solicitud.setFecha(fecha);
    System.out.println("Solicitud creada: " + solicitud);

    // Guardar la solicitud en la base de datos
    Solicitud solicitudGuardada = this.solicitudService.save(solicitud);
    System.out.println("Solicitud guardada en la base de datos: " + solicitudGuardada);

    // Crear la cadena de inserción SQL
    String sqlInsert = createSqlInsert(solicitudGuardada);
    System.out.println("Cadena de inserción SQL: " + sqlInsert);

    // Guardar la cadena de inserción en el archivo import.sql
    writeSqlInsertToFile(sqlInsert);
    
    // Incrementar el próximo ID
    nextId++;
    
    return solicitudGuardada;
}


    private String createSqlInsert(Solicitud solicitud) {
        // Obtener la fecha actual y formatearla como se requiere en la cadena de inserción
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String formattedDate = now.format(formatter);
        
        // Crear la cadena de inserción SQL con el ID incrementado
        String sqlInsert = String.format("INSERT INTO SOLICITUDES (id, tipo, fecha) VALUES (%d, '%s', '%s');",
                                         nextId, solicitud.getTipo(), formattedDate);
        return sqlInsert;
    }

    private void writeSqlInsertToFile(String sqlInsert) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("import.sql", true))) {
            writer.write(sqlInsert);
            writer.newLine();
        } catch (IOException e) {
            // Manejar la excepción si ocurre un error al escribir en el archivo
            e.printStackTrace();
        }
    }
}
