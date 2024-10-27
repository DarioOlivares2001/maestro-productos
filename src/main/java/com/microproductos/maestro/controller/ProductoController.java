package com.microproductos.maestro.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import com.microproductos.maestro.exception.ResourceNotFoundException;
import com.microproductos.maestro.model.Producto;
import com.microproductos.maestro.service.ProductoService;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/productos")
@Validated
public class ProductoController {
    private static final Logger log = LoggerFactory.getLogger(ProductoController.class);
    

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public CollectionModel<EntityModel<Producto>> listarProductos() {
        List<Producto> productos = productoService.listarProductos();
        log.info("GET /productos");
        log.info("Retornando todos los datos de productos");

        List<EntityModel<Producto>> productoResources = productos.stream()
            .map(producto -> EntityModel.of(producto,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).obtenerProducto(producto.getId())).withSelfRel()
            ))
            .collect(Collectors.toList());

        WebMvcLinkBuilder linkTo = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).listarProductos());
        CollectionModel<EntityModel<Producto>> resources = CollectionModel.of(productoResources, linkTo.withRel("productos"));

        return resources;
    }

    @PostMapping
    public ResponseEntity<Producto> crearProducto(@Valid @RequestBody Producto producto) {
        Producto nuevoProducto = productoService.guardarProducto(producto);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Long id) {
        Producto producto = productoService.obtenerProducto(id)
                .orElseThrow(() -> new ResourceNotFoundException("El producto con ID " + id + " no fue encontrado."));
        return ResponseEntity.ok(producto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.obtenerProducto(id)
                .orElseThrow(() -> new ResourceNotFoundException("El producto con ID " + id + " no fue encontrado."));
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @Valid @RequestBody Producto detallesProducto) {
        Producto producto = productoService.obtenerProducto(id)
                .orElseThrow(() -> new ResourceNotFoundException("El producto con ID " + id + " no fue encontrado."));
        
        producto.setNombre(detallesProducto.getNombre());
        producto.setDescripcion(detallesProducto.getDescripcion());
        producto.setPrecio(detallesProducto.getPrecio());
        producto.setCantidadStock(detallesProducto.getCantidadStock());

        Producto productoActualizado = productoService.guardarProducto(producto);
        return ResponseEntity.ok(productoActualizado);
    }
}
