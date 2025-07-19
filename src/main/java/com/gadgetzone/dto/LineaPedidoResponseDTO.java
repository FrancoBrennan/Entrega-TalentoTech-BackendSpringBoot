package com.gadgetzone.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LineaPedidoResponseDTO {
    private String nombreProducto;
    private Integer cantidad;
    private Double subtotal;
}
