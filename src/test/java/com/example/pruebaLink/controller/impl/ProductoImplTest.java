package com.example.pruebaLink.controller.impl;

import com.example.pruebaLink.BD.domain.Producto;
import com.example.pruebaLink.BD.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - ProductoImpl")
class ProductoImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoImpl productoService;

    private Producto productoValido;
    private Producto productoInvalido;
    private List<Producto> listaProductos;

    @BeforeEach
    void setUp() {
        // ✅ Datos de prueba
        productoValido = new Producto();
        productoValido.setId(1L);
        productoValido.setNombre("Laptop Dell");
        productoValido.setPrecio(BigDecimal.valueOf(1200.00));
        productoValido.setDescripcion("Laptop gaming");

        productoInvalido = new Producto();
        productoInvalido.setNombre(""); // Nombre inválido
        productoInvalido.setPrecio(BigDecimal.valueOf(500.00));

        listaProductos = List.of(
                new Producto(1L, "Laptop Dell", BigDecimal.valueOf(1200.00), "Gaming"),
                new Producto(2L, "Mouse Logitech", BigDecimal.valueOf(25.00), "Wireless")
        );

        // ✅ Inyectar repositorio por @Autowired
        ReflectionTestUtils.setField(productoService, "productoRepository", productoRepository);
    }

    @Test
    @DisplayName("getAllProductos - Éxito: Retorna lista de productos")
    void getAllProductos_Success() {
        // Arrange
        when(productoRepository.findAll()).thenReturn(listaProductos);

        // Act
        List<Producto> result = productoService.getAllProductos();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getNombre()).isEqualTo("Laptop Dell");
        verify(productoRepository).findAll();
    }

    @Test
    @DisplayName("getAllProductos - Lista vacía")
    void getAllProductos_EmptyList() {
        // Arrange
        when(productoRepository.findAll()).thenReturn(List.of());

        // Act
        List<Producto> result = productoService.getAllProductos();

        // Assert
        assertThat(result).isEmpty();
        verify(productoRepository).findAll();
    }

    @Test
    @DisplayName("getIdProductos - Éxito: Encuentra producto")
    void getIdProductos_Success() {
        // Arrange
        Long id = 1L;
        when(productoRepository.findById(id)).thenReturn(Optional.of(productoValido));

        // Act
        Optional<Producto> result = productoService.getIdProductos(id);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getNombre()).isEqualTo("Laptop Dell");
        verify(productoRepository).findById(id);
    }

    @Test
    @DisplayName("getIdProductos - No encontrado")
    void getIdProductos_NotFound() {
        // Arrange
        Long id = 999L;
        when(productoRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Producto> result = productoService.getIdProductos(id);

        // Assert
        assertThat(result).isEmpty();
        verify(productoRepository).findById(id);
    }

    @Test
    @DisplayName("createProducto - Éxito: Crea producto válido")
    void createProducto_Success() {
        // Arrange
        when(productoRepository.save(productoValido)).thenReturn(productoValido);

        // Act
        Producto result = productoService.createProducto(productoValido);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNombre()).isEqualTo("Laptop Dell");
        verify(productoRepository).save(productoValido);
    }

    @Test
    @DisplayName("createProducto - IllegalArgumentException: Nombre vacío")
    void createProducto_IllegalArgumentException() {
        // Act & Assert
        assertThatThrownBy(() -> productoService.createProducto(productoInvalido))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El nombre del producto es obligatorio");
        verifyNoInteractions(productoRepository);
    }

    @Test
    @DisplayName("updateProducto - Éxito: Actualiza producto existente")
    void updateProducto_Success() {
        // Arrange
        Long id = 1L;
        Producto productoActualizado = new Producto();
        productoActualizado.setNombre("Laptop Dell Actualizada");
        productoActualizado.setPrecio(BigDecimal.valueOf(1300.00));
        productoActualizado.setDescripcion("Gaming mejorada");

        when(productoRepository.findById(id)).thenReturn(Optional.of(productoValido));
        when(productoRepository.save(any(Producto.class))).thenReturn(productoValido);

        // Act
        Producto result = productoService.updateProducto(id, productoActualizado);

        // Assert
        assertThat(result.getNombre()).isEqualTo("Laptop Dell"); // Original, pero actualizado en BD
        verify(productoRepository).findById(id);
        verify(productoRepository).save(argThat(p ->
                p.getNombre().equals("Laptop Dell Actualizada") &&
                        p.getPrecio().equals(BigDecimal.valueOf(1300.00))
        ));
    }

    @Test
    @DisplayName("updateProducto - No encontrado")
    void updateProducto_NotFound() {
        // Arrange
        Long id = 999L;
        Producto productoActualizado = new Producto();
        productoActualizado.setNombre("Nuevo");

        when(productoRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> productoService.updateProducto(id, productoActualizado))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Producto no encontrado con ID: 999");
    }

    @Test
    @DisplayName("deleteProducto - Éxito: Elimina producto existente")
    void deleteProducto_Success() {
        // Arrange
        Long id = 1L;
        when(productoRepository.existsById(id)).thenReturn(true);

        // Act
        productoService.deleteProducto(id);

        // Assert
        verify(productoRepository).existsById(id);
        verify(productoRepository).deleteById(id);
    }

    @Test
    @DisplayName("deleteProducto - No encontrado")
    void deleteProducto_NotFound() {
        // Arrange
        Long id = 999L;
        when(productoRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> productoService.deleteProducto(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Producto no encontrado con ID: 999");
    }

    @Test
    @DisplayName("searchByNombre - Éxito: Encuentra productos")
    void searchByNombre_Success() {
        // Arrange
        String nombre = "lapt";
        when(productoRepository.findByNombreContainingIgnoreCase(nombre)).thenReturn(listaProductos);

        // Act
        List<Producto> result = productoService.searchByNombre(nombre);

        // Assert
        assertThat(result).hasSize(2);
        verify(productoRepository).findByNombreContainingIgnoreCase(nombre);
    }

    @Test
    @DisplayName("searchByNombre - Sin resultados")
    void searchByNombre_NoResults() {
        // Arrange
        String nombre = "xyz";
        when(productoRepository.findByNombreContainingIgnoreCase(nombre)).thenReturn(List.of());

        // Act
        List<Producto> result = productoService.searchByNombre(nombre);

        // Assert
        assertThat(result).isEmpty();
        verify(productoRepository).findByNombreContainingIgnoreCase(nombre);
    }

    @Test
    @DisplayName("Métodos lanzan RuntimeException en errores de repositorio")
    void services_ThrowRuntimeException_OnRepositoryError() {
        // Arrange - getAllProductos
        when(productoRepository.findAll()).thenThrow(new RuntimeException("DB Error"));

        // Act & Assert
        assertThatThrownBy(() -> productoService.getAllProductos())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("DB Error");
    }
}