package com.example.pruebaLink.controller.impl;

import com.example.pruebaLink.domain.EventoInventario;
import com.example.pruebaLink.domain.Inventario;
import com.example.pruebaLink.dto.InventarioDTO;
import com.example.pruebaLink.repository.EventoInventarioRepository;
import com.example.pruebaLink.repository.InventarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - InventarioImpl")
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
        inventarioDTO = new InventarioDTO(1L, 100);

        inventario = new Inventario();
        inventario.setId(1L);
        inventario.setProductoId(1L);
        inventario.setCantidad(BigDecimal.valueOf(100));
        inventario.setFechaActualizacion(LocalDateTime.now().toString());

        eventos = List.of(
                EventoInventario.builder()
                        .id(1L)
                        .inventarioId(1L)
                        .cantidad(100)
                        .tipoEvento("creacion")
                        .build()
        );

        // ✅ Inyectar repositorios manualmente (por @Autowired en clase real)
        ReflectionTestUtils.setField(inventarioService, "inventarioRepository", inventarioRepository);
        ReflectionTestUtils.setField(inventarioService, "eventoInventarioRepository", eventoInventarioRepository);
    }

    @Test
    @DisplayName("getInventarioProductoId - Éxito: Encuentra inventario")
    void getInventarioProductoId_Success() {
        // Arrange
        Long productoId = 1L;
        when(inventarioRepository.findByProductoId(productoId)).thenReturn(Optional.of(inventario));

        // Act
        Optional<Inventario> result = inventarioService.getInventarioProductoId(productoId);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(inventarioRepository).findByProductoId(productoId);
    }

    @Test
    @DisplayName("getInventarioProductoId - No encontrado")
    void getInventarioProductoId_NotFound() {
        // Arrange
        Long productoId = 999L;
        when(inventarioRepository.findByProductoId(productoId)).thenReturn(Optional.empty());

        // Act
        Optional<Inventario> result = inventarioService.getInventarioProductoId(productoId);

        // Assert
        assertThat(result).isEmpty();
        verify(inventarioRepository).findByProductoId(productoId);
    }

    @Test
    @DisplayName("addInventarioProductoId - Éxito: Crea nuevo inventario")
    void addInventarioProductoId_Success() {
        // Arrange
        when(inventarioRepository.findByProductoId(1L)).thenReturn(Optional.empty());
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventario);

        // Act
        Optional<Inventario> result = inventarioService.addInventarioProductoId(inventarioDTO);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getCantidad()).isEqualByComparingTo(BigDecimal.valueOf(100));
        verify(inventarioRepository).findByProductoId(1L);
        verify(inventarioRepository).save(any(Inventario.class));
    }

    @Test
    @DisplayName("addInventarioProductoId - Ya existe")
    void addInventarioProductoId_AlreadyExists() {
        // Arrange
        when(inventarioRepository.findByProductoId(1L)).thenReturn(Optional.of(inventario));

        // Act
        Optional<Inventario> result = inventarioService.addInventarioProductoId(inventarioDTO);

        // Assert
        assertThat(result).isPresent();
        verify(inventarioRepository).findByProductoId(1L);
        verifyNoInteractions(inventarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateInventarioProductoId - Éxito: Actualiza cantidad")
    void updateInventarioProductoId_Success() {
        // Arrange
        when(inventarioRepository.findByProductoId(1L)).thenReturn(Optional.of(inventario));
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventario);

        // Act
        Optional<Inventario> result = inventarioService.updateInventarioProductoId(inventarioDTO);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getCantidad()).isEqualByComparingTo(BigDecimal.valueOf(100));
        verify(inventarioRepository).save(argThat(inv ->
                inv.getCantidad().compareTo(BigDecimal.valueOf(200)) == 0));
    }

    @Test
    @DisplayName("updateInventarioProductoId - No encontrado")
    void updateInventarioProductoId_NotFound() {
        // Arrange
        when(inventarioRepository.findByProductoId(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Inventario> result = inventarioService.updateInventarioProductoId(
                new InventarioDTO(999L, 50));

        // Assert
        assertThat(result).isEmpty();
        verify(inventarioRepository).findByProductoId(999L);
    }

    @Test
    @DisplayName("getEventoInventarioId - Éxito: Retorna eventos")
    void getEventoInventarioId_Success() {
        // Arrange
        Long inventarioId = 1L;
        when(eventoInventarioRepository.findByInventarioIdOrderByFechaEventoDesc(inventarioId))
                .thenReturn(eventos);

        // Act
        List<EventoInventario> result = inventarioService.getEventoInventarioId(inventarioId);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTipoEvento()).isEqualTo("creacion");
        verify(eventoInventarioRepository).findByInventarioIdOrderByFechaEventoDesc(inventarioId);
    }

    @Test
    @DisplayName("getEventoInventarioId - Lista vacía")
    void getEventoInventarioId_EmptyList() {
        // Arrange
        Long inventarioId = 999L;
        when(eventoInventarioRepository.findByInventarioIdOrderByFechaEventoDesc(inventarioId))
                .thenReturn(List.of());

        // Act
        List<EventoInventario> result = inventarioService.getEventoInventarioId(inventarioId);

        // Assert
        assertThat(result).isEmpty();
        verify(eventoInventarioRepository).findByInventarioIdOrderByFechaEventoDesc(inventarioId);
    }

    @Test
    @DisplayName("Métodos lanzan RuntimeException en caso de error")
    void services_ThrowRuntimeException_OnError() {
        // Arrange
        Long id = 1L;
        when(inventarioRepository.findByProductoId(id))
                .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThatThrownBy(() -> inventarioService.getInventarioProductoId(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database error");
    }
}