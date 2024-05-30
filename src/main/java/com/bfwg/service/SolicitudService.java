package com.bfwg.service;
import java.util.List;
import com.bfwg.model.Solicitud;

public interface SolicitudService {

    Solicitud findById(Long id);

    List<Solicitud> findAll();

    Solicitud save(Solicitud solicitud);

    void delete(Long id);

    
}
