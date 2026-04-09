package com.example.pruebaLink.controller.impl;

import com.example.pruebaLink.BD.DTO.AgregarOrdenDTO;
import com.example.pruebaLink.BD.domain.OrdenServicio;
import com.example.pruebaLink.BD.domain.OrdenservicioProductos;
import com.example.pruebaLink.BD.domain.Producto;
import com.example.pruebaLink.BD.repository.OrdenServicioRepository;
import com.example.pruebaLink.BD.repository.OrdenservicioProductosRepository;
import com.example.pruebaLink.BD.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdenServicioImplTest {

    @Mock
    private OrdenServicioRepository ordenServicioRepository;

    @Mock
    private OrdenservicioProductosRepository ordenservicioProductosRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private OrdenServicioImpl ordenServicioService;

    private OrdenServicio ordenServicio;
    private Producto producto;
    private OrdenservicioProductos ordenProducto;
    private AgregarOrdenDTO agregarOrdenDTO;

    @BeforeEach
    void setUp() {
        ordenServicio = OrdenServicio.builder()
                .id(1L)
                .fecha(LocalDate.now())
                .cerrado(0)
                .total(BigDecimal.ZERO)
                .productos(Arrays.asList())
                .build();

        producto = Producto.builder()
                .id(1L)
                .nombre("Laptop")
                .build();

        ordenProducto = OrdenservicioProductos.builder()
                .id(1L)
                .ordenServicio(ordenServicio)
                .producto(producto)
                .cantidad(2)
                .costo(500.0)
                .build();

        agregarOrdenDTO = new AgregarOrdenDTO(1, 1, 2, 500.0);
    }

    @Test
    void newOrden_ShouldCreateAndReturnOrden() {
        // GIVEN
        when(ordenServicioRepository.save(any(OrdenServicio.class))).thenReturn(ordenServicio);

        // WHEN
        Optional<OrdenServicio> result = ordenServicioService.newOrden();

        // THEN
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals(0, result.get().getCerrado());
        verify(ordenServicioRepository, times(1)).save(any(OrdenServicio.class));
    }

    @Test
    void newOrden_ShouldThrowRuntimeException_OnError() {
        // GIVEN
        when(ordenServicioRepository.save(any(OrdenServicio.class)))
                .thenThrow(new RuntimeException("DB Error"));

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> ordenServicioService.newOrden());
        assertEquals("DB Error", exception.getCause().getMessage());
    }

    @Test
    void addProductoOrden_ShouldAddProduct_WhenValid() {
        // GIVEN
        when(ordenServicioRepository.findById(1L)).thenReturn(Optional.of(ordenServicio));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(ordenservicioProductosRepository.existsByOrdenServicioAndProducto(
                ordenServicio, producto)).thenReturn(false);
        when(ordenservicioProductosRepository.save(any(OrdenservicioProductos.class)))
                .thenReturn(ordenProducto);

        // WHEN
        Optional<OrdenServicio> result = ordenServicioService.addProductoOrden(agregarOrdenDTO);

        // THEN
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(ordenServicioRepository, times(2)).findById(1L); // Una para buscar, otra para save
        verify(productoRepository, times(1)).findById(1L);
        verify(ordenservicioProductosRepository, times(1)).existsByOrdenServicioAndProducto(
                ordenServicio, producto);
        verify(ordenservicioProductosRepository, times(1)).save(any(OrdenservicioProductos.class));
    }

    @Test
    void addProductoOrden_ShouldThrowIllegalArgumentException_WhenOrdenNotFound() {
        // GIVEN
        when(ordenServicioRepository.findById(999L)).thenReturn(Optional.empty());

        // WHEN & THEN
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ordenServicioService.addProductoOrden(new AgregarOrdenDTO(999, 1, 2, 500.0)));
        assertEquals("Orden de servicio no encontrada: 999", exception.getMessage());
    }

    @Test
    void addProductoOrden_ShouldThrowIllegalArgumentException_WhenProductNotFound() {
        // GIVEN
        when(ordenServicioRepository.findById(1L)).thenReturn(Optional.of(ordenServicio));
        when(productoRepository.findById(999L)).thenReturn(Optional.empty());

        // WHEN & THEN
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ordenServicioService.addProductoOrden(new AgregarOrdenDTO(1, 999, 2, 500.0)));
        assertEquals("Producto no encontrado: 999", exception.getMessage());
    }

    @Test
    void addProductoOrden_ShouldThrowIllegalArgumentException_WhenProductAlreadyExists() {
        // GIVEN
        when(ordenServicioRepository.findById(1L)).thenReturn(Optional.of(ordenServicio));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(ordenservicioProductosRepository.existsByOrdenServicioAndProducto(
                ordenServicio, producto)).thenReturn(true);

        // WHEN & THEN
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ordenServicioService.addProductoOrden(agregarOrdenDTO));
        assertEquals("El producto ya existe en esta orden de servicio", exception.getMessage());
    }

    @Test
    void consultaOrden_ShouldReturnOrden_WhenExists() {
        // GIVEN
        when(ordenServicioRepository.findById(1L)).thenReturn(Optional.of(ordenServicio));

        // WHEN
        Optional<OrdenServicio> result = ordenServicioService.consultaOrden(1L);

        // THEN
        assertTrue(result.isPresent());
        verify(ordenServicioRepository, times(1)).findById(1L);
    }

    @Test
    void consultaOrden_ShouldReturnEmpty_WhenNotExists() {
        // GIVEN
        when(ordenServicioRepository.findById(999L)).thenReturn(Optional.empty());

        // WHEN
        Optional<OrdenServicio> result = ordenServicioService.consultaOrden(999L);

        // THEN
        assertTrue(result.isEmpty());
        verify(ordenServicioRepository, times(1)).findById(999L);
    }

    @Test
    void cerrarOrden_ShouldCloseAndCalculateTotal_WhenValid() {
        // GIVEN
        ordenServicio.setProductos(Arrays.asList(ordenProducto)); // Agregar producto
        when(ordenServicioRepository.findById(1L)).thenReturn(Optional.of(ordenServicio));
        OrdenServicio ordenCerrada = OrdenServicio.builder()
                .id(1L)
                .cerrado(1)
                .total(BigDecimal.valueOf(1000.0)) // 2 * 500
                .build();
        when(ordenServicioRepository.save(any(OrdenServicio.class))).thenReturn(ordenCerrada);

        // WHEN
        Optional<OrdenServicio> result = ordenServicioService.cerrarOrden(1L);

        // THEN
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getCerrado());
        assertEquals(BigDecimal.valueOf(1000.0), result.get().getTotal());
        verify(ordenServicioRepository, times(2)).findById(1L);
        verify(ordenServicioRepository, times(1)).save(any(OrdenServicio.class));
    }

    @Test
    void cerrarOrden_ShouldThrowIllegalArgumentException_WhenNotFound() {
        // GIVEN
        when(ordenServicioRepository.findById(999L)).thenReturn(Optional.empty());

        // WHEN & THEN
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ordenServicioService.cerrarOrden(999L));
        assertEquals("Orden no encontrada: 999", exception.getMessage());
    }

    @Test
    void cerrarOrden_ShouldThrowIllegalStateException_WhenAlreadyClosed() {
        // GIVEN
        ordenServicio.setCerrado(1);
        when(ordenServicioRepository.findById(1L)).thenReturn(Optional.of(ordenServicio));

        // WHEN & THEN
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> ordenServicioService.cerrarOrden(1L));
        assertEquals("La orden ya está cerrada", exception.getMessage());
    }

    @Test
    void cerrarOrden_ShouldThrowIllegalStateException_WhenNoProducts() {
        // GIVEN
        when(ordenServicioRepository.findById(1L)).thenReturn(Optional.of(ordenServicio));

        // WHEN & THEN
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> ordenServicioService.cerrarOrden(1L));
        assertEquals("No se puede cerrar una orden sin productos", exception.getMessage());
    }
}