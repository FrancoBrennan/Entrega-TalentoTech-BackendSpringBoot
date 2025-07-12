package com.gadgetzone.controller;

import com.gadgetzone.entity.LineaPedido;
import com.gadgetzone.entity.Pedido;
import com.gadgetzone.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.gadgetzone.dto.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping("/crear")
    public ResponseEntity<PedidoDTO> crearPedido(@RequestParam Long usuarioId, @RequestBody List<LineaPedido> items) {
        Pedido pedido = pedidoService.crearPedido(usuarioId, items);
        PedidoDTO dto = mapToDTO(pedido);
        return ResponseEntity.ok(dto);
    }


    @GetMapping("/usuario/{usuarioId}")
    public List<Pedido> pedidosPorUsuario(@PathVariable Long usuarioId) {
        return pedidoService.pedidosPorUsuario(usuarioId);
    }

    private PedidoDTO mapToDTO(Pedido pedido) {
        List<LineaPedidoDTO> lineas = pedido.getLineas().stream().map(linea ->
                LineaPedidoDTO.builder()
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

}
