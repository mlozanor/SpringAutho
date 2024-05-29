package com.bfwg.repository;

import com.bfwg.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long>{
    Producto findByTipo( String tipo );
    
}
