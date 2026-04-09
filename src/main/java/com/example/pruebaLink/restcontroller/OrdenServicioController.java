package com.example.pruebaLink.restcontroller;

import com.example.pruebaLink.BD.DTO.AgregarOrdenDTO;
import com.example.pruebaLink.BD.domain.OrdenServicio;
import com.example.pruebaLink.BD.domain.Producto;
import com.example.pruebaLink.controller.services.OrdenServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/ordenes")
@RequiredArgsConstructor
public class OrdenServicioController {

    private final OrdenServicioService ordenServicioService;

    @PostMapping("/neworden")
    public ResponseEntity<OrdenServicio> crearOrden() {
        OrdenServicio orden = ordenServicioService.newOrden().orElseThrow(() -> new RuntimeException("Error creando orden"));
        return ResponseEntity.status(HttpStatus.CREATED).body(orden);
    }

    @PostMapping("/productos")
    public ResponseEntity<OrdenServicio> agregarProducto(@RequestBody AgregarOrdenDTO request) {
        OrdenServicio ordenActualizada = ordenServicioService.addProductoOrden(request)
                .orElseThrow(() -> new RuntimeException("Error agregando producto"));
        return ResponseEntity.ok(ordenActualizada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenServicio> obtenerOrden(@PathVariable Long id) {
        OrdenServicio orden = ordenServicioService.consultaOrden(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Orden no encontrada: " + id));
        return ResponseEntity.ok(orden);
    }

    @PutMapping("/{id}/cerrar")
    public ResponseEntity<OrdenServicio> cerrarOrden(@PathVariable Long id) {
        OrdenServicio ordenCerrada = ordenServicioService.cerrarOrden(id)
                .orElseThrow(() -> new RuntimeException("Error cerrando orden"));
        return ResponseEntity.ok(ordenCerrada);
    }
}