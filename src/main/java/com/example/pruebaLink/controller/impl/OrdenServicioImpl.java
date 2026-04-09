package com.example.pruebaLink.controller.impl;

import com.example.pruebaLink.BD.DTO.AgregarOrdenDTO;
import com.example.pruebaLink.BD.domain.OrdenServicio;
import com.example.pruebaLink.BD.domain.OrdenservicioProductos;
import com.example.pruebaLink.BD.domain.Producto;
import com.example.pruebaLink.BD.repository.OrdenServicioRepository;
import com.example.pruebaLink.BD.repository.OrdenservicioProductosRepository;
import com.example.pruebaLink.BD.repository.ProductoRepository;
import com.example.pruebaLink.controller.services.OrdenServicioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrdenServicioImpl implements OrdenServicioService {

    @Autowired
    private OrdenServicioRepository ordenServicioRepository;

    @Autowired
    private OrdenservicioProductosRepository ordenservicioProductosRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public Optional<OrdenServicio> newOrden() {
        try{
            OrdenServicio orden = OrdenServicio.builder()
                    .fecha(LocalDateTime.now().toLocalDate())
                    .cerrado(0)
                    .build();
            OrdenServicio saved = ordenServicioRepository.save(orden);
            log.info("Orden creada exitosamente: {} con total: {}",
                    saved.getId(), saved.getTotal());
            return Optional.of(saved);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<OrdenServicio> addProductoOrden(AgregarOrdenDTO request) {
        log.info("Creando producto en la orden: {}", request.ordenId());
        try {
            OrdenServicio ordenServicio = ordenServicioRepository.findById(request.ordenId().longValue())
                    .orElseThrow(() -> new IllegalArgumentException("Orden de servicio no encontrada: " + request.ordenId()));
            Producto producto = productoRepository.findById(request.productoId().longValue())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + request.productoId()));
            boolean yaExiste = ordenservicioProductosRepository.existsByOrdenServicioAndProducto(
                    ordenServicio, producto);
            if (yaExiste) {
                throw new IllegalArgumentException("El producto ya existe en esta orden de servicio");
            }

            OrdenservicioProductos nuevo = new OrdenservicioProductos();
            nuevo.setOrdenServicio(ordenServicio);
            nuevo.setProducto(producto);
            nuevo.setCantidad(request.cantidad());
            nuevo.setCosto(request.costo());
            OrdenservicioProductos saved = ordenservicioProductosRepository.save(nuevo);
            log.info("Producto creado con ID: {}", saved.getId());
            ordenServicio.getProductos().add(saved);
            ordenServicioRepository.save(ordenServicio);
            return Optional.of(ordenServicio);

        } catch (IllegalArgumentException e) {
            log.error("ERROR de validación: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("ERROR creando producto en la orden '{}': {}", request.ordenId(), e.getMessage(), e);
            throw new RuntimeException("No se pudo agregar el producto a la orden", e);
        }
    }

    @Override
    public Optional<OrdenServicio> consultaOrden(long id) {
        log.info("Consultando Orden: {}", id);
        try {
            Optional<OrdenServicio> orden = ordenServicioRepository.findById(id);
            return orden;
        } catch (Exception e) {
            log.error("ERROR consultando Orden: {}, {}", id, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public Optional<OrdenServicio> cerrarOrden(long id) {
        log.info("Cerrando orden de servicio: {}", id);
        try {
            OrdenServicio orden = ordenServicioRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada: " + id));
            if (orden.getCerrado() == 1) {
                throw new IllegalStateException("La orden ya está cerrada");
            }
            if (orden.getProductos() == null || orden.getProductos().isEmpty()) {
                throw new IllegalStateException("No se puede cerrar una orden sin productos");
            }
            Double total = orden.getProductos().stream()
                    .mapToDouble(producto ->
                            producto.getCantidad() * producto.getCosto())
                    .sum();
            orden.setTotal(BigDecimal.valueOf(total));
            orden.setCerrado(1);
            OrdenServicio saved = ordenServicioRepository.save(orden);

            log.info("Orden {} cerrada exitosamente | Total calculado: ${}",
                    saved.getId(), saved.getTotal());

            return Optional.of(saved);

        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Error de validación cerrando orden {}: {}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error cerrando orden {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("No se pudo cerrar la orden", e);
        }
    }
}
