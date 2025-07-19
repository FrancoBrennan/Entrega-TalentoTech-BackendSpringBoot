package com.gadgetzone.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LineaPedidoRequestDTO {
    private Long productoId;
    private Integer cantidad;
}

