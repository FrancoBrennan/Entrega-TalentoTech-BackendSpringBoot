package com.gadgetzone.dto;

import com.gadgetzone.Enum.Role;
import lombok.Data;

@Data
public class RegisterRequestDTO {
    private String nombre;
    private String username;
    private String password;
    private Role role;
}

