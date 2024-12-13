package com.microproductos.maestro.exception;

import com.microproductos.maestro.model.Producto;
import com.microproductos.maestro.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class GlobalExceptionHandlerTest {

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
    void testHandleValidationExceptions() throws Exception {
        // Realiza una solicitud POST con un cuerpo inválido
        String invalidProductoJson = "{\"nombre\":\"\", \"precio\":-100.0, \"image\":\"\"}";

        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidProductoJson))
                .andExpect(status().isBadRequest())  // Verifica el código 400
                .andExpect(jsonPath("$.nombre").value("El nombre debe tener entre 1 y 100 caracteres"))
                .andExpect(jsonPath("$.precio").value("El precio no puede ser negativo"))
                .andExpect(jsonPath("$.image").value("La imagen debe ser un path válido"));
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
