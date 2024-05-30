package com.bfwg.rest;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bfwg.model.Solicitud;
import com.bfwg.service.SolicitudService;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class SolicitudController {

    private SolicitudService solicitudService;

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

    @GetMapping("/solicitud/create")
    @PreAuthorize("hasRole('USER')")
    public Solicitud createSolicitud(@RequestParam String tipo, @RequestParam String fecha) {
        Solicitud solicitud = new Solicitud();
        solicitud.setTipo(tipo);
        solicitud.setFecha(fecha);
        return this.solicitudService.save(solicitud);
        
    }
    

  
    
}

