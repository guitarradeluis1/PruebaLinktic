package com.example.pruebaLink.controller.impl;

import com.example.pruebaLink.BD.DTO.AgregarOrdenDTO;
import com.example.pruebaLink.BD.domain.OrdenServicio;
import com.example.pruebaLink.BD.domain.OrdenservicioProductos;
import com.example.pruebaLink.BD.domain.Producto;
import com.example.pruebaLink.BD.repository.OrdenServicioRepository;
import com.example.pruebaLink.BD.repository.OrdenservicioProductosRepository;
import com.example.pruebaLink.controller.services.OrdenServicioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrdenServicioImpl implements OrdenServicioService {

    @Autowired
    private OrdenServicioRepository ordenServicioRepository;

    @Autowired
    private OrdenservicioProductosRepository ordenservicioProductosRepository;

    @Override
    public Optional<OrdenServicio> newOrden() {
        return Optional.empty();
    }

    @Override
    public Optional<OrdenServicio> addProductoOrden(AgregarOrdenDTO request) {
        log.info("Creando producto en la orden: {}", request.ordenId());
        try {
            OrdenservicioProductos nuevo = new OrdenservicioProductos();
            nuevo.setProductoId(Long.valueOf(request.productoId()));
            //nuevo.setOrdenservicioId(Long.valueOf(request.ordenId()));
            nuevo.setCantidad(request.cantidad());
            nuevo.setCosto(Double.valueOf(request.costo()));
            OrdenservicioProductos saved = null;
            //saved = ordenservicioProductosRepository.save(nuevo);
            //log.info("Producto creado con ID: {}", saved.getId());
            //return saved;
            return Optional.empty();
        } catch (IllegalArgumentException e) {
            log.error("ERROR de validación: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("ERROR creando producto  en la orden'{}': {}", request.ordenId(), e.getMessage());
            throw new RuntimeException(e);
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
}
