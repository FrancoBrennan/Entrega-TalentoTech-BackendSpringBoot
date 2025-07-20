package com.gadgetzone.controller;

import com.gadgetzone.dto.ProductoResponseDTO;
import com.gadgetzone.entity.Producto;
import com.gadgetzone.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.gadgetzone.dto.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    public ProductoResponseDTO crearProducto(@RequestBody ProductoRequestDTO request) {
        Producto producto = Producto.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .precio(request.getPrecio())
                .categoria(request.getCategoria())
                .imagenUrl(request.getImagenUrl())
                .stock(request.getStock())
                .build();

        Producto guardado = productoService.guardar(producto);
        return mapToDTO(guardado);
    }


    @GetMapping
    public List<ProductoResponseDTO> listarProductos() {
        return productoService.listarTodos().stream()
                .map(this::mapToDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerProducto(@PathVariable Long id) {
        return productoService.obtenerPorId(id)
                .map(producto -> ResponseEntity.ok(mapToDTO(producto)))
                .orElse(ResponseEntity.notFound().build());
    }

    private ProductoResponseDTO mapToDTO(Producto producto) {
        return ProductoResponseDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .categoria(producto.getCategoria())
                .imagenUrl(producto.getImagenUrl())
                .stock(producto.getStock())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizarProducto(
            @PathVariable Long id,
            @RequestBody ProductoRequestDTO request) {

        Producto actualizado = productoService.actualizar(id, request);
        return ResponseEntity.ok(mapToDTO(actualizado));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }


}
