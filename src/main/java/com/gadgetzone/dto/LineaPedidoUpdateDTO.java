package com.gadgetzone.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LineaPedidoUpdateDTO {
    private Long productoId;
    private int cantidad;
}
