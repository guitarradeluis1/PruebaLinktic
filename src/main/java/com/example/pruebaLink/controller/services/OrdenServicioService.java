package com.example.pruebaLink.controller.services;

import com.example.pruebaLink.BD.DTO.AgregarOrdenDTO;
import com.example.pruebaLink.BD.domain.OrdenServicio;

import java.util.Optional;

public interface OrdenServicioService {
    Optional<OrdenServicio> newOrden();
    Optional<OrdenServicio> addProductoOrden(AgregarOrdenDTO request);
    Optional<OrdenServicio> consultaOrden(long id);
    Optional<OrdenServicio> cerrarOrden(long id);
}
