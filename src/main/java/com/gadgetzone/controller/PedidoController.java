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
    public ResponseEntity<PedidoResponseDTO> crearPedido(@RequestBody PedidoRequestDTO request) {
        Usuario usuario = authService.getAuthenticatedUser(); // JWT
        Pedido pedido = pedidoService.crearPedidoDesdeRequest(request.getItems(), usuario);
        PedidoResponseDTO response = mapToDTO(pedido); // Conversión a DTO
        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/usuario/{usuarioId}")
    public List<Pedido> pedidosPorUsuario(@PathVariable Long usuarioId) {
        return pedidoService.pedidosPorUsuario(usuarioId);
    }


    private PedidoResponseDTO mapToDTO(Pedido pedido) {
        List<LineaPedidoResponseDTO> lineas = pedido.getLineas().stream().map(linea ->
                LineaPedidoResponseDTO.builder()
                        .nombreProducto(linea.getProducto().getNombre())
                        .cantidad(linea.getCantidad())
                        .subtotal(linea.getSubtotal())
                        .build()
        ).toList();

        return PedidoResponseDTO.builder()
                .id(pedido.getId())
                .fecha(pedido.getFecha())
                .total(pedido.getTotal())
                .estado(pedido.getEstado())
                .nombreUsuario(pedido.getUsuario().getNombre())
                .lineas(lineas)
                .build();
    }

    @GetMapping("/mis-pedidos")
    public ResponseEntity<List<PedidoResponseDTO>> obtenerMisPedidos() {
        Usuario usuario = authService.getAuthenticatedUser();
        List<Pedido> pedidos = pedidoService.obtenerPedidosDeUsuario(usuario);

        List<PedidoResponseDTO> respuesta = pedidos.stream()
                .map(this::mapToDTO)
                .toList();

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PedidoResponseDTO>> obtenerTodosLosPedidos() {
        List<Pedido> pedidos = pedidoService.obtenerTodos();
        List<PedidoResponseDTO> respuesta = pedidos.stream()
                .map(this::mapToDTO)
                .toList();

        return ResponseEntity.ok(respuesta);
    }

    @PutMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PedidoResponseDTO> actualizarEstadoPedido(
            @PathVariable Long id,
            @RequestBody ActualizarEstadoPedidoRequestDTO request) {

        Pedido actualizado = pedidoService.actualizarEstado(id, request.getEstado());
        return ResponseEntity.ok(mapToDTO(actualizado));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        pedidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/lineas")
    public ResponseEntity<PedidoResponseDTO> actualizarLineasPedido(
            @PathVariable Long id,
            @RequestBody PedidoUpdateRequestDTO request) {

        Pedido actualizado = pedidoService.actualizarLineasPedido(id, request);
        return ResponseEntity.ok(mapToDTO(actualizado));
    }



}
