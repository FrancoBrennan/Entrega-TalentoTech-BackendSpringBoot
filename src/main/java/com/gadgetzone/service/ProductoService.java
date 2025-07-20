package com.gadgetzone.service;

import com.gadgetzone.dto.ProductoRequestDTO;
import com.gadgetzone.entity.Producto;
import com.gadgetzone.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepo;

    public ProductoService(ProductoRepository productoRepo) {
        this.productoRepo = productoRepo;
    }

    public Producto guardar(Producto producto) {
        return productoRepo.save(producto);
    }

    public List<Producto> listarTodos() {
        return productoRepo.findAll();
    }

    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepo.findById(id);
    }

    public Producto actualizar(Long id, ProductoRequestDTO request) {
        Producto producto = productoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setCategoria(request.getCategoria());
        producto.setImagenUrl(request.getImagenUrl());
        producto.setStock(request.getStock());

        return productoRepo.save(producto);
    }

    public void eliminar(Long id) {
        productoRepo.deleteById(id);
    }

}
