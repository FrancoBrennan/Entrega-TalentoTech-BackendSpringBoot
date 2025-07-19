package com.gadgetzone.service;

import com.gadgetzone.Auth.AuthService;
import com.gadgetzone.entity.*;
import com.gadgetzone.exception.StockInsuficienteException;
import com.gadgetzone.repository.*;
import com.gadgetzone.dto.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepo;
    private final ProductoRepository productoRepo;
    private final UsuarioRepository usuarioRepo;
    private final AuthService authService;

    private final LineaPedidoRepository lineaPedidoRepo;

    public PedidoService(PedidoRepository pedidoRepo, ProductoRepository productoRepo, UsuarioRepository usuarioRepo, AuthService authService, LineaPedidoRepository lineaPedidoRepo) {
        this.pedidoRepo = pedidoRepo;
        this.productoRepo = productoRepo;
        this.usuarioRepo = usuarioRepo;
        this.authService = authService;
        this.lineaPedidoRepo = lineaPedidoRepo;
    }

    public Pedido crearPedidoDesdeRequest(List<LineaPedidoRequestDTO> items, Usuario usuario) {
        List<LineaPedido> lineas = items.stream().map(req -> {
            Producto producto = productoRepo.findById(req.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            if (producto.getStock() < req.getCantidad()) {
                throw new StockInsuficienteException("Stock insuficiente para " + producto.getNombre());
            }

            producto.setStock(producto.getStock() - req.getCantidad());

            LineaPedido linea = new LineaPedido();
            linea.setProducto(producto);
            linea.setCantidad(req.getCantidad());
            linea.setSubtotal(producto.getPrecio() * req.getCantidad());
            return linea;
        }).toList();

        return crearPedido(lineas, usuario);
    }

    public Pedido crearPedido(List<LineaPedido> lineas, Usuario usuario) {
        double total = lineas.stream()
                .mapToDouble(LineaPedido::getSubtotal)
                .sum();

        Pedido pedido = Pedido.builder()
                .usuario(usuario)
                .fecha(LocalDateTime.now())
                .estado("PENDIENTE")
                .total(total)
                .build();

        pedido = pedidoRepo.save(pedido);

        for (LineaPedido linea : lineas) {
            linea.setPedido(pedido);
            lineaPedidoRepo.save(linea);
        }

        pedido.setLineas(lineas);
        return pedidoRepo.save(pedido);
    }



    public List<Pedido> pedidosPorUsuario(Long usuarioId) {
        return pedidoRepo.findByUsuarioId(usuarioId);
    }

    public List<Pedido> obtenerPedidosDeUsuario(Usuario usuario) {
        return pedidoRepo.findByUsuario(usuario);
    }

    public List<Pedido> obtenerTodos() {
        return pedidoRepo.findAll();
    }


}


