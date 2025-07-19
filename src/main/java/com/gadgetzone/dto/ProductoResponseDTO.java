package com.gadgetzone.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductoResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private String categoria;
    private String imagenUrl;
    private Integer stock;
}
