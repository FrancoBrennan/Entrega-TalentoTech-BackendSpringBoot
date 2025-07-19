package com.gadgetzone.dto;

import lombok.*;

@Data
@Builder
public class LineaPedidoDTO {
    private String productoNombre;
    private int cantidad;
    private double precioUnitario;
}

