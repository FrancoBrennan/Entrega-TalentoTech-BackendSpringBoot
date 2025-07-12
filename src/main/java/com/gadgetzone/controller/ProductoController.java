package com.gadgetzone.controller;

import com.gadgetzone.dto.ProductoDTO;
import com.gadgetzone.entity.Producto;
import com.gadgetzone.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    public ProductoDTO crearProducto(@RequestBody Producto producto) {
        Producto guardado = productoService.guardar(producto);
        return mapToDTO(guardado);
    }

    @GetMapping
    public List<ProductoDTO> listarProductos() {
        return productoService.listarTodos().stream()
                .map(this::mapToDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerProducto(@PathVariable Long id) {
        return productoService.obtenerPorId(id)
                .map(producto -> ResponseEntity.ok(mapToDTO(producto)))
                .orElse(ResponseEntity.notFound().build());
    }

    private ProductoDTO mapToDTO(Producto producto) {
        return ProductoDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .categoria(producto.getCategoria())
                .imagenUrl(producto.getImagenUrl())
                .stock(producto.getStock())
                .build();
    }
}
