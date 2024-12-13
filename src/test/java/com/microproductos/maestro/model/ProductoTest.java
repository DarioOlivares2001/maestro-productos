package com.microproductos.maestro.model;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ProductoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        // Inicializa el validador de la clase
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testProductoValido() {
        Producto producto = new Producto();
        producto.setNombre("Producto 1");
        producto.setPrecio(100.0);
        producto.setImage("/images/producto1.jpg");

        Set<ConstraintViolation<Producto>> violations = validator.validate(producto);

        // Verifica que no haya violaciones
        assertTrue(violations.isEmpty(), "No deben haber violaciones de validación");
    }

    @Test
    void testProductoNombreNulo() {
        Producto producto = new Producto();
        producto.setPrecio(100.0);
        producto.setImage("/images/producto1.jpg");

        Set<ConstraintViolation<Producto>> violations = validator.validate(producto);

        assertFalse(violations.isEmpty(), "El nombre no puede ser nulo");

        // Verifica que la violación sea por el campo "nombre"
        assertEquals(1, violations.size());
        assertEquals("El nombre no puede ser nulo", violations.iterator().next().getMessage());
    }

    @Test
    void testProductoPrecioNegativo() {
        Producto producto = new Producto();
        producto.setNombre("Producto 1");
        producto.setPrecio(-100.0);
        producto.setImage("/images/producto1.jpg");

        Set<ConstraintViolation<Producto>> violations = validator.validate(producto);

        assertFalse(violations.isEmpty(), "El precio no puede ser negativo");

        // Verifica que la violación sea por el campo "precio"
        assertEquals(1, violations.size());
        assertEquals("El precio no puede ser negativo", violations.iterator().next().getMessage());
    }

    @Test
    void testProductoImagenNula() {
        Producto producto = new Producto();
        producto.setNombre("Producto 1");
        producto.setPrecio(100.0);

        Set<ConstraintViolation<Producto>> violations = validator.validate(producto);

        assertFalse(violations.isEmpty(), "La imagen no puede ser nula");

        // Verifica que la violación sea por el campo "image"
        assertEquals(1, violations.size());
        assertEquals("La imagen no puede ser nula", violations.iterator().next().getMessage());
    }
}
