package com.gadgetzone.service;

import com.gadgetzone.entity.*;
import com.gadgetzone.exception.StockInsuficienteException;
import com.gadgetzone.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepo;
    private final ProductoRepository productoRepo;
    private final UsuarioRepository usuarioRepo;

    public PedidoService(PedidoRepository pedidoRepo, ProductoRepository productoRepo, UsuarioRepository usuarioRepo) {
        this.pedidoRepo = pedidoRepo;
        this.productoRepo = productoRepo;
        this.usuarioRepo = usuarioRepo;
    }

    public Pedido crearPedido(Long usuarioId, List<LineaPedido> items) {
        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<LineaPedido> lineasFinales = new ArrayList<>();
        double total = 0;

        for (LineaPedido linea : items) {
            Producto producto = productoRepo.findById(linea.getProducto().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            if (producto.getStock() < linea.getCantidad()) {
                throw new StockInsuficienteException("Stock insuficiente para " + producto.getNombre());
            }

            // Actualiza el stock
            producto.setStock(producto.getStock() - linea.getCantidad());
            productoRepo.save(producto);

            double subtotal = linea.getCantidad() * producto.getPrecio();
            total += subtotal;

            linea.setSubtotal(subtotal);
            linea.setProducto(producto);
            lineasFinales.add(linea);
        }

        Pedido pedido = Pedido.builder()
                .usuario(usuario)
                .fecha(LocalDateTime.now())
                .estado("PENDIENTE")
                .total(total)
                .lineas(new ArrayList<>()) // se llena luego para evitar error de dependencia circular
                .build();

        pedido = pedidoRepo.save(pedido);

        for (LineaPedido linea : lineasFinales) {
            linea.setPedido(pedido);
        }

        pedido.setLineas(lineasFinales);
        return pedidoRepo.save(pedido);
    }

    public List<Pedido> pedidosPorUsuario(Long usuarioId) {
        return pedidoRepo.findByUsuarioId(usuarioId);
    }
}
