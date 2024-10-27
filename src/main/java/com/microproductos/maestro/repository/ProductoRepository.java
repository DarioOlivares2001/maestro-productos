package com.microproductos.maestro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.microproductos.maestro.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
