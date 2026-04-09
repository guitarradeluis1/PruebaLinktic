package com.example.pruebaLink.controller.impl;

import com.example.pruebaLink.BD.DTO.InventarioDTO;
import com.example.pruebaLink.BD.domain.EventoInventario;
import com.example.pruebaLink.BD.domain.Inventario;
import com.example.pruebaLink.BD.repository.EventoInventarioRepository;
import com.example.pruebaLink.BD.repository.InventarioRepository;
import com.example.pruebaLink.controller.services.InventarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioImplTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @Mock
    private EventoInventarioRepository eventoInventarioRepository;

    @InjectMocks
    private InventarioImpl inventarioService;

    private InventarioDTO inventarioDTO;
    private Inventario inventario;
    private List<EventoInventario> eventos;

    @BeforeEach
    void setUp() {
        inventarioDTO = new InventarioDTO(1, 100);
        inventario = Inventario.builder()
                .id(1L)
                .productoId(1L)
                .cantidad(BigDecimal.valueOf(100))
                .fechaActualizacion(LocalDateTime.now().toString())
                .build();

        EventoInventario evento1 = EventoInventario.builder()
                .id(1L)
                .inventarioId(1L)
                .tipoEvento("ENTRADA")
                .cantidad(BigDecimal.valueOf(50))
                .fechaEvento(LocalDateTime.now())
                .build();

        EventoInventario evento2 = EventoInventario.builder()
                .id(2L)
                .inventarioId(1L)
                .tipoEvento("SALIDA")
                .cantidad(BigDecimal.valueOf(20))
                .fechaEvento(LocalDateTime.now().minusDays(1))
                .build();

        eventos = Arrays.asList(evento1, evento2);
    }

    @Test
    void getInventarioProductoId_ShouldReturnInventario_WhenExists() {
        // GIVEN
        when(inventarioRepository.findByProductoId(1L)).thenReturn(Optional.of(inventario));

        // WHEN
        Optional<Inventario> result = inventarioService.getInventarioProductoId(1L);

        // THEN
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getProductoId());
        assertEquals(BigDecimal.valueOf(100), result.get().getCantidad());
        verify(inventarioRepository, times(1)).findByProductoId(1L);
    }

    @Test
    void getInventarioProductoId_ShouldReturnEmpty_WhenNotExists() {
        // GIVEN
        when(inventarioRepository.findByProductoId(999L)).thenReturn(Optional.empty());

        // WHEN
        Optional<Inventario> result = inventarioService.getInventarioProductoId(999L);

        // THEN
        assertTrue(result.isEmpty());
        verify(inventarioRepository, times(1)).findByProductoId(999L);
    }

    @Test
    void getInventarioProductoId_ShouldThrowRuntimeException_OnError() {
        // GIVEN
        when(inventarioRepository.findByProductoId(1L))
                .thenThrow(new RuntimeException("Database error"));

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> inventarioService.getInventarioProductoId(1L));
        assertEquals("Database error", exception.getCause().getMessage());
    }

    @Test
    void addInventarioProductoId_ShouldCreateNew_WhenNotExists() {
        // GIVEN
        when(inventarioRepository.findByProductoId(1L)).thenReturn(Optional.empty());
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventario);

        // WHEN
        Optional<Inventario> result = inventarioService.addInventarioProductoId(inventarioDTO);

        // THEN
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getProductoId());
        assertEquals(BigDecimal.valueOf(100), result.get().getCantidad());
        verify(inventarioRepository, times(1)).findByProductoId(1L);
        verify(inventarioRepository, times(1)).save(any(Inventario.class));
    }

    @Test
    void addInventarioProductoId_ShouldReturnExisting_WhenAlreadyExists() {
        // GIVEN
        when(inventarioRepository.findByProductoId(1L)).thenReturn(Optional.of(inventario));

        // WHEN
        Optional<Inventario> result = inventarioService.addInventarioProductoId(inventarioDTO);

        // THEN
        assertTrue(result.isPresent());
        verify(inventarioRepository, times(1)).findByProductoId(1L);
        verify(inventarioRepository, never()).save(any(Inventario.class));
    }

    @Test
    void updateInventarioProductoId_ShouldUpdate_WhenExists() {
        // GIVEN
        when(inventarioRepository.findByProductoId(1L)).thenReturn(Optional.of(inventario));
        Inventario updated = Inventario.builder()
                .id(1L)
                .productoId(1L)
                .cantidad(BigDecimal.valueOf(150))  // 100 + 50
                .build();
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(updated);

        InventarioDTO updateDTO = new InventarioDTO(1, 50);

        // WHEN
        Optional<Inventario> result = inventarioService.updateInventarioProductoId(updateDTO);

        // THEN
        assertTrue(result.isPresent());
        assertEquals(BigDecimal.valueOf(150), result.get().getCantidad());
        verify(inventarioRepository, times(1)).findByProductoId(1L);
        verify(inventarioRepository, times(1)).save(any(Inventario.class));
    }

    @Test
    void updateInventarioProductoId_ShouldReturnEmpty_WhenNotExists() {
        // GIVEN
        when(inventarioRepository.findByProductoId(999L)).thenReturn(Optional.empty());

        // WHEN
        Optional<Inventario> result = inventarioService.updateInventarioProductoId(inventarioDTO);

        // THEN
        assertTrue(result.isEmpty());
        verify(inventarioRepository, times(1)).findByProductoId(999L);
        verify(inventarioRepository, never()).save(any(Inventario.class));
    }

    @Test
    void getEventoInventarioId_ShouldReturnEvents() {
        // GIVEN
        when(eventoInventarioRepository.findByInventarioIdOrderByFechaEventoDesc(1L))
                .thenReturn(eventos);

        // WHEN
        List<EventoInventario> result = inventarioService.getEventoInventarioId(1L);

        // THEN
        assertEquals(2, result.size());
        assertEquals("ENTRADA", result.get(0).getTipoEvento());
        assertEquals("SALIDA", result.get(1).getTipoEvento());
        verify(eventoInventarioRepository, times(1))
                .findByInventarioIdOrderByFechaEventoDesc(1L);
    }

    @Test
    void getEventoInventarioId_ShouldReturnEmptyList_OnNoEvents() {
        // GIVEN
        when(eventoInventarioRepository.findByInventarioIdOrderByFechaEventoDesc(999L))
                .thenReturn(List.of());

        // WHEN
        List<EventoInventario> result = inventarioService.getEventoInventarioId(999L);

        // THEN
        assertTrue(result.isEmpty());
        verify(eventoInventarioRepository, times(1))
                .findByInventarioIdOrderByFechaEventoDesc(999L);
    }

    @Test
    void getEventoInventarioId_ShouldThrowRuntimeException_OnError() {
        // GIVEN
        when(eventoInventarioRepository.findByInventarioIdOrderByFechaEventoDesc(1L))
                .thenThrow(new RuntimeException("DB Error"));

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> inventarioService.getEventoInventarioId(1L));
        assertEquals("DB Error", exception.getCause().getMessage());
    }
}