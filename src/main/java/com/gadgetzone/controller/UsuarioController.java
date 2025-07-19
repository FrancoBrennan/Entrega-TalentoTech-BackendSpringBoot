package com.gadgetzone.controller;

import com.gadgetzone.entity.Usuario;
import com.gadgetzone.service.UsuarioService;
import org.springframework.web.bind.annotation.*;
import com.gadgetzone.dto.UsuarioResponseDTO;
import com.gadgetzone.dto.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public UsuarioResponseDTO crear(@RequestBody UsuarioRequestDTO request) {
        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        Usuario guardado = usuarioService.guardar(usuario);

        return UsuarioResponseDTO.builder()
                .id(guardado.getId())
                .nombre(guardado.getNombre())
                .build();
    }

    @GetMapping
    public List<UsuarioResponseDTO> listar() {
        return usuarioService.listar().stream()
                .map(u -> new UsuarioResponseDTO(u.getId(), u.getNombre()))
                .toList();
    }
}

