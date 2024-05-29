package com.bfwg.repository;

import com.bfwg.model.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudRepository  extends JpaRepository<Solicitud, Long>{
    Solicitud findByTipo( String tipo );

    

    
}
