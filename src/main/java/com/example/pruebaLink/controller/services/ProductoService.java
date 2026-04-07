package com.example.pruebaLink.controller.services;

import com.example.pruebaLink.BD.domain.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoService {
    List<Producto> getAllProductos();
    Optional<Producto> getIdProductos(Long id);
    Producto createProducto(Producto producto);
    Producto updateProducto(Long id, Producto producto);
    void deleteProducto(Long id);
    List<Producto> searchByNombre(String nombre);
}
