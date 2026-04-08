package com.example.pruebaLink.controller.impl;

import com.example.pruebaLink.BD.DTO.AgregarOrdenDTO;
import com.example.pruebaLink.BD.domain.OrdenServicio;
import com.example.pruebaLink.BD.domain.OrdenservicioProductos;
import com.example.pruebaLink.BD.repository.OrdenServicioRepository;
import com.example.pruebaLink.BD.repository.OrdenservicioProductosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - OrdenServicioImpl")
class OrdenServicioImplTest {

    @Mock
    private OrdenServicioRepository ordenServicioRepository;

    @Mock
    private OrdenservicioProductosRepository ordenservicioProductosRepository;

    @InjectMocks
    private OrdenServicioImpl ordenServicioService;

    private AgregarOrdenDTO agregarOrdenDTO;
    private OrdenServicio ordenServicio;
    private OrdenservicioProductos productoOrden;

    @BeforeEach
    void setUp() {
        // ✅ Datos de prueba
        agregarOrdenDTO = new AgregarOrdenDTO(1L, 1L, 2, 150.50);

        ordenServicio = new OrdenServicio();
        ordenServicio.setId(1L);

        productoOrden = new OrdenservicioProductos();
        productoOrden.setId(1L);
        productoOrden.setProductoId(1L);
        productoOrden.setCantidad(2);
        productoOrden.setCosto(BigDecimal.valueOf(150.50));

        // ✅ Inyectar repositorios (por @Autowired)
        ReflectionTestUtils.setField(ordenServicioService, "ordenServicioRepository", ordenServicioRepository);
        ReflectionTestUtils.setField(ordenServicioService, "ordenservicioProductosRepository", ordenservicioProductosRepository);
    }

    @Test
    @DisplayName("newOrden - Retorna Optional vacío")
    void newOrden_ReturnsEmpty() {
        // Act
        Optional<OrdenServicio> result = ordenServicioService.newOrden();

        // Assert
        assertThat(result).isEmpty();
        verifyNoInteractions(ordenServicioRepository, ordenservicioProductosRepository);
    }

    @Test
    @DisplayName("addProductoOrden - Éxito: Crea producto en orden")
    void addProductoOrden_Success() {
        // Arrange
        when(ordenservicioProductosRepository.save(any(OrdenservicioProductos.class)))
                .thenReturn(productoOrden);

        // Act
        Optional<OrdenServicio> result = ordenServicioService.addProductoOrden(agregarOrdenDTO);

        // Assert
        assertThat(result).isPresent(); // Aunque esté comentado, probamos lógica
        verify(ordenservicioProductosRepository).save(argThat(producto ->
                producto.getProductoId().equals(1L) &&
                        producto.getCantidad().equals(2) &&
                        producto.getCosto().equals(BigDecimal.valueOf(150.50))
        ));
    }

    @Test
    @DisplayName("addProductoOrden - IllegalArgumentException")
    void addProductoOrden_IllegalArgumentException() {
        // Arrange
        AgregarOrdenDTO invalidDTO = new AgregarOrdenDTO(null, 1L, 0, 100.0);

        // Act & Assert
        assertThatThrownBy(() -> ordenServicioService.addProductoOrden(invalidDTO))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("addProductoOrden - RuntimeException por error repositorio")
    void addProductoOrden_RuntimeException() {
        // Arrange
        when(ordenservicioProductosRepository.save(any()))
                .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThatThrownBy(() -> ordenServicioService.addProductoOrden(agregarOrdenDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database error");
    }

    @Test
    @DisplayName("consultaOrden - Éxito: Encuentra orden")
    void consultaOrden_Success() {
        // Arrange
        long ordenId = 1L;
        when(ordenServicioRepository.findById(ordenId)).thenReturn(Optional.of(ordenServicio));

        // Act
        Optional<OrdenServicio> result = ordenServicioService.consultaOrden(ordenId);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(ordenServicioRepository).findById(ordenId);
    }

    @Test
    @DisplayName("consultaOrden - No encontrado")
    void consultaOrden_NotFound() {
        // Arrange
        long ordenId = 999L;
        when(ordenServicioRepository.findById(ordenId)).thenReturn(Optional.empty());

        // Act
        Optional<OrdenServicio> result = ordenServicioService.consultaOrden(ordenId);

        // Assert
        assertThat(result).isEmpty();
        verify(ordenServicioRepository).findById(ordenId);
    }

    @Test
    @DisplayName("consultaOrden - RuntimeException por error repositorio")
    void consultaOrden_RuntimeException() {
        // Arrange
        long ordenId = 1L;
        when(ordenServicioRepository.findById(ordenId))
                .thenThrow(new RuntimeException("Database connection failed"));

        // Act & Assert
        assertThatThrownBy(() -> ordenServicioService.consultaOrden(ordenId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database connection failed");
    }
}