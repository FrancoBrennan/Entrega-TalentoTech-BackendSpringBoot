package com.gadgetzone.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioResponseDTO {
    private Long id;
    private String nombre;
}
