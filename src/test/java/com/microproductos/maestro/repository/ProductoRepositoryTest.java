package com.microproductos.maestro.repository;

import com.microproductos.maestro.model.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductoRepositoryTest {

    @Autowired
    private ProductoRepository productoRepository;

    private Producto producto;

    @BeforeEach
    void setUp() {
        // Configura un producto para las pruebas
        producto = new Producto();
        producto.setNombre("Producto 1");
        producto.setPrecio(100.0);
        producto.setImage("/images/producto1.jpg");

        // Guardar el producto en la base de datos
        producto = productoRepository.save(producto);
    }

    @Test
    void testSaveProducto() {
        // Guardamos el producto y verificamos si la base de datos lo persiste correctamente
        assertNotNull(producto.getId(), "El producto debe tener un ID asignado");
        assertEquals("Producto 1", producto.getNombre());
        assertEquals(100.0, producto.getPrecio());
        assertEquals("/images/producto1.jpg", producto.getImage());
    }

    @Test
    void testFindById() {
        // Buscamos el producto por ID y verificamos si se obtiene correctamente
        Producto productoEncontrado = productoRepository.findById(producto.getId()).orElse(null);
        assertNotNull(productoEncontrado, "El producto debe ser encontrado");
        assertEquals(producto.getId(), productoEncontrado.getId());
    }

    @Test
    void testUpdateProducto() {
        // Actualizamos el precio del producto
        producto.setPrecio(150.0);
        productoRepository.save(producto);

        Producto productoActualizado = productoRepository.findById(producto.getId()).orElse(null);
        assertNotNull(productoActualizado, "El producto debe ser encontrado");
        assertEquals(150.0, productoActualizado.getPrecio(), "El precio del producto debe haber sido actualizado");
    }

    @Test
    void testDeleteProducto() {
        // Eliminar el producto
        Long productoId = producto.getId();
        productoRepository.deleteById(productoId);

        Producto productoEliminado = productoRepository.findById(productoId).orElse(null);
        assertNull(productoEliminado, "El producto debe haber sido eliminado");
    }

    @Test
    void testFindAllProductos() {
        // Verificamos que el producto que hemos guardado se encuentra en la lista
        Producto producto2 = new Producto();
        producto2.setNombre("Producto 2");
        producto2.setPrecio(200.0);
        producto2.setImage("/images/producto2.jpg");
        productoRepository.save(producto2);

        assertTrue(productoRepository.findAll().size() > 1, "La lista de productos debe tener más de un elemento");
    }
}
