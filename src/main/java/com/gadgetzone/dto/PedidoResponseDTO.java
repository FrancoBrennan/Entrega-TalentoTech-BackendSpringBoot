package com.gadgetzone.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class PedidoResponseDTO {
    private Long id;
    private LocalDateTime fecha;
    private Double total;
    private String estado;
    private String nombreUsuario;
    private List<LineaPedidoResponseDTO> lineas;
}

