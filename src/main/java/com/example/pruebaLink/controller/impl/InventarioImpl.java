package com.example.pruebaLink.controller.impl;

import com.example.pruebaLink.BD.DTO.InventarioDTO;
import com.example.pruebaLink.BD.domain.EventoInventario;
import com.example.pruebaLink.BD.domain.Inventario;
import com.example.pruebaLink.BD.domain.Producto;
import com.example.pruebaLink.BD.repository.EventoInventarioRepository;
import com.example.pruebaLink.BD.repository.InventarioRepository;
import com.example.pruebaLink.controller.services.InventarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventarioImpl implements InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private EventoInventarioRepository eventoInventarioRepository;

    @Override
    public Optional<Inventario> getInventarioProductoId(Long id) {
        log.info("Consultando Inventario por Producto: {}", id);
        try {
            Optional<Inventario> invt = inventarioRepository.findByProductoId(id);
            if (invt.isPresent()) {
                log.info("Inventario encontrado: Inventario {}", id);
            } else {
                log.warn("Inventario no encontrado con Producto {}", id);
            }
            return invt;
        } catch (Exception e) {
            log.error("ERROR consultando Inventario por producto ID {}: {}", id, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Inventario> addInventarioProductoId(InventarioDTO data) {
        log.info("Actualización Inventario por Producto: {}", data.id());
        try {
            Optional<Inventario> invt = inventarioRepository.findByProductoId(data.id().longValue());
            if (invt.isPresent()) {
                log.warn("Inventario ya existes para actualizar con Producto {}", data.id().longValue());
            } else {
                Inventario inventario = new Inventario();
                inventario.setProductoId(data.id().longValue());
                inventario.setCantidad(BigDecimal.valueOf(data.cantidad()));
                inventario.setFechaActualizacion(LocalDateTime.now().toString());
                Inventario inventarioActualizado = inventarioRepository.save(inventario);
                log.info("Inventario creado exitosamente:", inventarioActualizado.toString() );
                return Optional.of(inventarioActualizado);
            }
            return invt;
        } catch (Exception e) {
            log.error("ERROR consultando Inventario por producto ID {}: {}", data.id(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Inventario> updateInventarioProductoId(InventarioDTO data) {
        log.info("Actualización Inventario por Producto: {}", data.id().longValue());
        try {
            Optional<Inventario> invt = inventarioRepository.findByProductoId(data.id().longValue());
            if (invt.isPresent()) {
                log.info("Inventario encontrado para actualizar: Inventario {}", data.id());
                Inventario inventario = invt.get();
                inventario.setCantidad(BigDecimal.valueOf(inventario.getCantidad().intValue() + data.cantidad()));
                Inventario inventarioActualizado = inventarioRepository.save(inventario);
                log.info("Inventario actualizado exitosamente: ID={}, Nueva Cantidad={}",
                        inventarioActualizado.getId(), inventarioActualizado.getCantidad());
                return Optional.of(inventarioActualizado);
            } else {
                log.warn("Inventario no encontrado para actualizar con Producto {}", data.id());
            }
            return invt;
        } catch (Exception e) {
            log.error("ERROR consultando Inventario por producto ID {}: {}", data.id(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<EventoInventario> getEventoInventarioId(Long id) {
        log.info("Consultando Eventos por Inventario: {}", id);
        try {
            List<EventoInventario> eventos = eventoInventarioRepository.findByInventarioIdOrderByFechaEventoDesc(id);
            return eventos;
        } catch (Exception e) {
            log.error("ERROR consultando Eventos por Inventario ID {}: {}", id, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
