package com.example.pruebaLink.controller.services;

import com.example.pruebaLink.BD.DTO.InventarioDTO;
import com.example.pruebaLink.BD.domain.EventoInventario;
import com.example.pruebaLink.BD.domain.Inventario;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface InventarioService {
    Optional<Inventario> getInventarioProductoId(Long id);
    Optional<Inventario> addInventarioProductoId(InventarioDTO request);
    Optional<Inventario> updateInventarioProductoId(InventarioDTO request);
    List<EventoInventario> getEventoInventarioId(Long id);
}
