package com.microproductos.maestro.controller;

import com.microproductos.maestro.model.Producto;
import com.microproductos.maestro.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class ProductoControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Configura un producto de ejemplo
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Producto 1");
        producto.setPrecio(100.0);
        producto.setImage("/images/producto1.jpg");
    }

    @Test
    void testListarProductos() throws Exception {
        // Simula la respuesta del servicio
        when(productoService.listarProductos()).thenReturn(Arrays.asList(producto));

        // Realiza la solicitud GET y verifica la respuesta
        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Producto 1"))
                .andExpect(jsonPath("$[0].precio").value(100.0))
                .andExpect(jsonPath("$[0].image").value("/images/producto1.jpg"));

        // Verifica que el servicio haya sido llamado una vez
        verify(productoService, times(1)).listarProductos();
    }

    @Test
    void testCrearProducto() throws Exception {
        // Simula la respuesta del servicio
        when(productoService.guardarProducto(Mockito.any(Producto.class))).thenReturn(producto);

        // Realiza la solicitud POST y verifica la respuesta
        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Producto 1\", \"precio\":100.0, \"image\":\"/images/producto1.jpg\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Producto 1"))
                .andExpect(jsonPath("$.precio").value(100.0));

        // Verifica que el servicio haya sido llamado una vez
        verify(productoService, times(1)).guardarProducto(Mockito.any(Producto.class));
    }

    @Test
    void testObtenerProducto() throws Exception {
        // Simula la respuesta del servicio
        when(productoService.obtenerProducto(1L)).thenReturn(Optional.of(producto));

        // Realiza la solicitud GET y verifica la respuesta
        mockMvc.perform(get("/api/productos/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Producto 1"))
                .andExpect(jsonPath("$.precio").value(100.0));

        // Verifica que el servicio haya sido llamado una vez
        verify(productoService, times(1)).obtenerProducto(1L);
    }

    @Test
    void testActualizarProducto() throws Exception {
        // Simula la respuesta del servicio
        when(productoService.obtenerProducto(1L)).thenReturn(Optional.of(producto));
        when(productoService.guardarProducto(Mockito.any(Producto.class))).thenReturn(producto);

        // Realiza la solicitud PUT y verifica la respuesta
        mockMvc.perform(put("/api/productos/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Producto 1 actualizado\", \"precio\":120.0, \"image\":\"/images/producto1_updated.jpg\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Producto 1 actualizado"))
                .andExpect(jsonPath("$.precio").value(120.0));

        // Verifica que el servicio haya sido llamado una vez
        verify(productoService, times(1)).obtenerProducto(1L);
        verify(productoService, times(1)).guardarProducto(Mockito.any(Producto.class));
    }

    @Test
    void testEliminarProducto() throws Exception {
        // Simula la respuesta del servicio
        when(productoService.obtenerProducto(1L)).thenReturn(Optional.of(producto));

        // Realiza la solicitud DELETE y verifica la respuesta
        mockMvc.perform(delete("/api/productos/{id}", 1L))
                .andExpect(status().isNoContent());

        // Verifica que el servicio haya sido llamado una vez
        verify(productoService, times(1)).obtenerProducto(1L);
        verify(productoService, times(1)).eliminarProducto(1L);
    }

    @Test
    void testCrearProductosMasivo() throws Exception {
        // Simula la respuesta del servicio para múltiples productos
        when(productoService.guardarProducto(Mockito.any(Producto.class))).thenReturn(producto);

        // Realiza la solicitud POST masiva y verifica la respuesta
        mockMvc.perform(post("/api/productos/masivo")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{\"nombre\":\"Producto 1\", \"precio\":100.0, \"image\":\"/images/producto1.jpg\"}, " +
                        "{\"nombre\":\"Producto 2\", \"precio\":150.0, \"image\":\"/images/producto2.jpg\"}]"))
                .andExpect(status().isCreated());

        // Verifica que el servicio haya sido llamado para cada producto
        verify(productoService, times(2)).guardarProducto(Mockito.any(Producto.class));
    }
}
