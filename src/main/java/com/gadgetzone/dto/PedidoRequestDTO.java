package com.gadgetzone.dto;

import lombok.Data;

import java.util.List;

@Data
public class PedidoRequestDTO {
    private List<LineaPedidoRequestDTO> items;
}


