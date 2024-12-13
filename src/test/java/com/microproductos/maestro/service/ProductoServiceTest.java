package com.microproductos.maestro.service;

import com.microproductos.maestro.model.Producto;
import com.microproductos.maestro.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configura un producto para las pruebas
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Producto 1");
        producto.setPrecio(100.0);
        producto.setImage("/images/producto1.jpg");
    }

    @Test
    void testListarProductos() {
        // Simula que el repositorio devuelve una lista de productos
        when(productoRepository.findAll()).thenReturn(List.of(producto));

        List<Producto> productos = productoService.listarProductos();

        // Verifica que la lista no esté vacía y contenga el producto correcto
        assertNotNull(productos);
        assertEquals(1, productos.size());
        assertEquals("Producto 1", productos.get(0).getNombre());

        // Verifica que el repositorio haya sido llamado
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void testObtenerProducto() {
        // Simula que el repositorio encuentra un producto por su ID
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Optional<Producto> productoEncontrado = productoService.obtenerProducto(1L);

        // Verifica que el producto se haya encontrado correctamente
        assertTrue(productoEncontrado.isPresent());
        assertEquals("Producto 1", productoEncontrado.get().getNombre());

        // Verifica que el repositorio haya sido llamado
        verify(productoRepository, times(1)).findById(1L);
    }

    @Test
    void testGuardarProducto() {
        // Simula que el repositorio guarda un producto y lo devuelve
        when(productoRepository.save(producto)).thenReturn(producto);

        Producto productoGuardado = productoService.guardarProducto(producto);

        // Verifica que el producto guardado sea el mismo que el que se pasó al servicio
        assertNotNull(productoGuardado);
        assertEquals("Producto 1", productoGuardado.getNombre());

        // Verifica que el repositorio haya sido llamado
        verify(productoRepository, times(1)).save(producto);
    }

    @Test
    void testEliminarProducto() {
        // Simula que el producto existe en el repositorio
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        // Llamamos al método eliminarProducto
        productoService.eliminarProducto(1L);

        // Verifica que el repositorio haya sido llamado para eliminar el producto
        verify(productoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testObtenerProductoNoEncontrado() {
        // Simula que el producto no se encuentra en el repositorio
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Producto> productoNoEncontrado = productoService.obtenerProducto(1L);

        // Verifica que no se encuentre el producto
        assertFalse(productoNoEncontrado.isPresent(), "El producto no debe estar presente");

        // Verifica que el repositorio haya sido llamado
        verify(productoRepository, times(1)).findById(1L);
    }
}
