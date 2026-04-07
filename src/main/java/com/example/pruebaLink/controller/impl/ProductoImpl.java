package com.example.pruebaLink.controller.impl;

import com.example.pruebaLink.BD.domain.Producto;
import com.example.pruebaLink.BD.repository.ProductoRepository;
import com.example.pruebaLink.controller.services.ProductoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductoImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<Producto> getAllProductos() {
        log.info("Consultando todos los productos");
        try {
            List<Producto> productos = productoRepository.findAll();
            return productos;
        } catch (Exception e) {
            log.error("ERROR consultando todos los productos: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Producto> getIdProductos(Long id) {
        log.info("Consultando producto por ID: {}", id);
        try {
            Optional<Producto> producto = productoRepository.findById(id);
            if (producto.isPresent()) {
                log.info("Producto encontrado: ID {} - {}", id, producto.get().getNombre());
            } else {
                log.warn("Producto no encontrado con ID: {}", id);
            }
            return producto;
        } catch (Exception e) {
            log.error("ERROR consultando producto ID {}: {}", id, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Producto createProducto(Producto producto) {
        log.info("Creando producto: {}", producto.getNombre());
        try {
            if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del producto es obligatorio");
            }
            Producto saved = productoRepository.save(producto);
            log.info("Producto creado con ID: {}", saved.getId());
            return saved;
        } catch (IllegalArgumentException e) {
            log.error("ERROR de validación: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("ERROR creando producto '{}': {}", producto.getNombre(), e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Override
    public Producto updateProducto(Long id, Producto productoActualizado) {
        log.info("actualizando producto ID: {} con nombre: {}", id, productoActualizado.getNombre());
        try {
            Optional<Producto> productoExistente = productoRepository.findById(id);
            if (productoExistente.isEmpty()) {
                log.warn("Producto ID {} no encontrado para actualizar", id);
                throw new RuntimeException("Producto no encontrado con ID: " + id);
            }
            Producto producto = productoExistente.get();
            producto.setNombre(productoActualizado.getNombre());
            producto.setPrecio(productoActualizado.getPrecio());
            producto.setDescripcion(productoActualizado.getDescripcion());

            Producto updated = productoRepository.save(producto);
            log.info("Producto ID {} actualizado correctamente", id);
            return updated;
        } catch (Exception e) {
            log.error("ERROR actualizando producto ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al actualizar producto ID " + id, e);
        }
    }

    @Override
    public void deleteProducto(Long id) {
        log.info("eliminando producto ID: {}", id);
        try {
            if (!productoRepository.existsById(id)) {
                log.warn("Producto ID {} no existe para eliminar", id);
                throw new RuntimeException("Producto no encontrado con ID: " + id);
            }
            productoRepository.deleteById(id);
            log.info("Producto ID {} eliminado correctamente", id);
        } catch (Exception e) {
            log.error("ERROR eliminando producto ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al eliminar producto ID " + id, e);
        }
    }

    @Override
    public List<Producto> searchByNombre(String nombre) {
        log.info("Buscando productos por nombre: '{}'", nombre);
        try {
            List<Producto> productos = productoRepository.findByNombreContainingIgnoreCase(nombre);
            log.info("Encontrados {} productos para '{}'", productos.size(), nombre);
            return productos;
        } catch (Exception e) {
            log.error("ERROR buscando productos por '{}': {}", nombre, e.getMessage());
            throw new RuntimeException("Error en búsqueda de productos", e);
        }
    }
}
