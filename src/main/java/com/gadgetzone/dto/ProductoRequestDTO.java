package com.gadgetzone.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductoRequestDTO {
    private String nombre;
    private String descripcion;
    private Double precio;
    private String categoria;
    private String imagenUrl;
    private Integer stock;
}
