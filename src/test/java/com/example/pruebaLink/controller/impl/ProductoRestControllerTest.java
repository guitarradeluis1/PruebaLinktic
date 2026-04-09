package com.example.pruebaLink.controller.impl;

import com.example.pruebaLink.BD.domain.Producto;
import com.example.pruebaLink.controller.services.ProductoService;
import com.example.pruebaLink.restcontroller.ProductoRestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductoRestControllerTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoRestController productoRestController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Producto producto1;
    private Producto producto2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productoRestController).build();
        objectMapper = new ObjectMapper();

        // Datos de prueba
        producto1 = Producto.builder()
                .id(1L)
                .nombre("Laptop Dell")
                .precio(1200.0)
                .build();

        producto2 = Producto.builder()
                .id(2L)
                .nombre("Mouse Logitech")
                .precio(25.99)
                .build();
    }

    @Test
    void inicio_ShouldReturnApiOk() throws Exception {
        // WHEN & THEN
        mockMvc.perform(get("/productos/estado"))
                .andExpect(status().isOk())
                .andExpect(content().string("API OK"));
    }

    @Test
    void getAllProductos_ShouldReturnAllProducts() throws Exception {
        // GIVEN
        List<Producto> productos = Arrays.asList(producto1, producto2);
        when(productoService.getAllProductos()).thenReturn(productos);

        // WHEN & THEN
        mockMvc.perform(get("/productos/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Laptop Dell"))
                .andExpect(jsonPath("$[1].nombre").value("Mouse Logitech"));

        verify(productoService, times(1)).getAllProductos();
    }

    @Test
    void getAllProductos_ShouldReturnEmptyList() throws Exception {
        // GIVEN
        when(productoService.getAllProductos()).thenReturn(List.of());

        // WHEN & THEN
        mockMvc.perform(get("/productos/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getProductoById_ShouldReturnProduct() throws Exception {
        // GIVEN
        when(productoService.getIdProductos(1L)).thenReturn(Optional.of(producto1));

        // WHEN & THEN
        mockMvc.perform(get("/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Laptop Dell"))
                .andExpect(jsonPath("$.precio").value(1200.0));

        verify(productoService, times(1)).getIdProductos(1L);
    }

    @Test
    void getProductoById_ShouldReturnNotFound() throws Exception {
        // GIVEN
        when(productoService.getIdProductos(999L)).thenReturn(Optional.empty());

        // WHEN & THEN
        mockMvc.perform(get("/productos/999"))
                .andExpect(status().isNotFound());

        verify(productoService, times(1)).getIdProductos(999L);
    }

    @Test
    void createProducto_ShouldReturnCreated() throws Exception {
        // GIVEN
        when(productoService.createProducto(any(Producto.class)))
                .thenReturn(producto1);

        // WHEN & THEN
        mockMvc.perform(post("/productos/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Laptop Dell"));

        verify(productoService, times(1)).createProducto(any(Producto.class));
    }

    @Test
    void createProducto_ShouldReturnBadRequest_OnError() throws Exception {
        // GIVEN
        when(productoService.createProducto(any(Producto.class)))
                .thenThrow(new RuntimeException("Error"));

        // WHEN & THEN
        mockMvc.perform(post("/productos/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto1)))
                .andExpect(status().isBadRequest());

        verify(productoService, times(1)).createProducto(any(Producto.class));
    }

    @Test
    void deleteProducto_ShouldReturnNoContent() throws Exception {
        // GIVEN
        doNothing().when(productoService).deleteProducto(1L);

        // WHEN & THEN
        mockMvc.perform(delete("/productos/1"))
                .andExpect(status().isNoContent());

        verify(productoService, times(1)).deleteProducto(1L);
    }

    @Test
    void deleteProducto_ShouldReturnNotFound() throws Exception {
        // GIVEN
        doThrow(new RuntimeException("Not found")).when(productoService).deleteProducto(999L);

        // WHEN & THEN
        mockMvc.perform(delete("/productos/999"))
                .andExpect(status().isNotFound());

        verify(productoService, times(1)).deleteProducto(999L);
    }

    @Test
    void searchProductos_ShouldReturnMatchingProducts() throws Exception {
        // GIVEN
        List<Producto> resultados = Arrays.asList(producto1);
        when(productoService.searchByNombre("Laptop")).thenReturn(resultados);

        // WHEN & THEN
        mockMvc.perform(get("/productos/search")
                        .param("nombre", "Laptop"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Laptop Dell"));

        verify(productoService, times(1)).searchByNombre("Laptop");
    }

    @Test
    void searchProductos_ShouldReturnEmpty_OnNoMatch() throws Exception {
        // GIVEN
        when(productoService.searchByNombre("xyz")).thenReturn(List.of());

        // WHEN & THEN
        mockMvc.perform(get("/productos/search")
                        .param("nombre", "xyz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
}