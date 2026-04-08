package com.example.pruebaLink.restcontroller;

import com.example.pruebaLink.BD.DTO.InventarioDTO;
import com.example.pruebaLink.BD.domain.EventoInventario;
import com.example.pruebaLink.BD.domain.Inventario;
import com.example.pruebaLink.controller.services.InventarioService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/inventario")
@RequiredArgsConstructor
public class InventarioRestController {

    private final InventarioService inventarioService;

    @GetMapping("/{productoId}")
    public ResponseEntity<?> getInventarioPorProductoId(@PathVariable Long productoId) {
        return inventarioService.getInventarioProductoId(productoId)
                .map(inventario -> ResponseEntity.ok(inventario))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<Inventario> newInventarioPorProductoId(@RequestBody InventarioDTO request) {
        Optional<Inventario> resultado = inventarioService.addInventarioProductoId(request);
        if (resultado.isPresent()) {
            return ResponseEntity.ok(resultado.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateInventarioPorProductoId(@RequestBody InventarioDTO request) {
        Optional<Inventario> resultado = inventarioService.updateInventarioProductoId(request);
        if (resultado.isPresent()) {
            return ResponseEntity.ok(resultado.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/eventos/{inventarioId}")
    public ResponseEntity<List<EventoInventario>> getEventosPorInventarioId(@PathVariable Long inventarioId) {
        List<EventoInventario> eventos = inventarioService.getEventoInventarioId(inventarioId);
        return ResponseEntity.ok(eventos);
    }
}
