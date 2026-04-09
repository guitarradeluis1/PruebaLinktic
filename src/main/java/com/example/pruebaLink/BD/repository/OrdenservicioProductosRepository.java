package com.example.pruebaLink.BD.repository;

import com.example.pruebaLink.BD.domain.OrdenServicio;
import com.example.pruebaLink.BD.domain.OrdenservicioProductos;
import com.example.pruebaLink.BD.domain.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdenservicioProductosRepository extends JpaRepository<OrdenservicioProductos, Long> {
    boolean existsByOrdenServicioAndProducto(OrdenServicio ordenServicio, Producto producto);
}