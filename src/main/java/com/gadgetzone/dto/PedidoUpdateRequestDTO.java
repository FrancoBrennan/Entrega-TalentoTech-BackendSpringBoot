package com.gadgetzone.dto;

import lombok.Data;

import java.util.List;

@Data
public class PedidoUpdateRequestDTO {
    private List<LineaPedidoDTO> lineas;
}

