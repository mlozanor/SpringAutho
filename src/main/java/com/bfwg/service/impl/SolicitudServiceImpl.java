package com.bfwg.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import com.bfwg.model.Solicitud;
import com.bfwg.repository.SolicitudRepository;
import com.bfwg.service.SolicitudService;


@Service
public class SolicitudServiceImpl implements SolicitudService {

    protected final Log LOGGER = LogFactory.getLog(getClass());

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Override
    public Solicitud findById(Long id) throws AccessDeniedException {
        Solicitud u = solicitudRepository.findById(id).orElse(null);
        return u;
    }

    public List<Solicitud> findAll() throws AccessDeniedException {
        List<Solicitud> result = solicitudRepository.findAll();
        return result;
    }

    public Solicitud save(Solicitud solicitud) {
        return solicitudRepository.save(solicitud);
    }

    public void delete(Long id) {
        solicitudRepository.deleteById(id);
    }

   

    
}
