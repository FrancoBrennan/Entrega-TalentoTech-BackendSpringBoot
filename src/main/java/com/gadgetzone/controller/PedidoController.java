package com.gadgetzone.controller;

import com.gadgetzone.Auth.AuthService;
import com.gadgetzone.entity.Pedido;
import com.gadgetzone.entity.Usuario;
import com.gadgetzone.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.gadgetzone.dto.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final AuthService authService;

    public PedidoController(PedidoService pedidoService, AuthService authService) {
        this.pedidoService = pedidoService;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<PedidoDTO> crearPedido(@RequestBody PedidoRequestDTO request) {
        Usuario usuario = authService.getAuthenticatedUser(); // JWT
        Pedido pedido = pedidoService.crearPedidoDesdeRequest(request.getItems(), usuario);
        PedidoDTO response = mapToDTO(pedido); // Conversión a DTO
        return ResponseEntity.ok(response);
    }




    @GetMapping("/usuario/{usuarioId}")
    public List<Pedido> pedidosPorUsuario(@PathVariable Long usuarioId) {
        return pedidoService.pedidosPorUsuario(usuarioId);
    }

    private PedidoDTO mapToDTO(Pedido pedido) {
        List<LineaPedidoResponseDTO> lineas = pedido.getLineas().stream().map(linea ->
                LineaPedidoResponseDTO.builder()
                        .nombreProducto(linea.getProducto().getNombre())
                        .cantidad(linea.getCantidad())
                        .subtotal(linea.getSubtotal())
                        .build()
        ).toList();

        return PedidoDTO.builder()
                .id(pedido.getId())
                .fecha(pedido.getFecha())
                .total(pedido.getTotal())
                .estado(pedido.getEstado())
                .nombreUsuario(pedido.getUsuario().getNombre())
                .lineas(lineas)
                .build();
    }

    @GetMapping("/mis-pedidos")
    public ResponseEntity<List<PedidoDTO>> obtenerMisPedidos() {
        Usuario usuario = authService.getAuthenticatedUser();
        List<Pedido> pedidos = pedidoService.obtenerPedidosDeUsuario(usuario);

        List<PedidoDTO> respuesta = pedidos.stream()
                .map(this::mapToDTO)
                .toList();

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PedidoDTO>> obtenerTodosLosPedidos() {
        List<Pedido> pedidos = pedidoService.obtenerTodos();
        List<PedidoDTO> respuesta = pedidos.stream()
                .map(this::mapToDTO)
                .toList();

        return ResponseEntity.ok(respuesta);
    }



}
