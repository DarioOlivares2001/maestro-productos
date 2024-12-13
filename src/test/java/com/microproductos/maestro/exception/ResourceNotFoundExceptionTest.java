package com.microproductos.maestro.exception;

import com.microproductos.maestro.model.Producto;
import com.microproductos.maestro.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class ResourceNotFoundExceptionTest {

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
    void testHandleResourceNotFoundException() throws Exception {
        // Simula que no se encuentra el producto
        when(productoService.obtenerProducto(1L)).thenReturn(Optional.empty());

        // Realiza la solicitud GET para un producto que no existe
        mockMvc.perform(get("/api/productos/{id}", 1L))
                .andExpect(status().isNotFound())  // Verifica el código 404
                .andExpect(content().string("Producto con ID 1 no encontrado"));  // Verifica el mensaje de error
    }
}
