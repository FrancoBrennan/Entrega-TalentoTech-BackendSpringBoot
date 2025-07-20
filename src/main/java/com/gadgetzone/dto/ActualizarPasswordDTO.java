package com.gadgetzone.dto;

import lombok.Data;

@Data
public class ActualizarPasswordDTO {
    private String passwordActual;
    private String nuevaPassword;
}
