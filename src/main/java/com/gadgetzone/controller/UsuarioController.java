package com.gadgetzone.controller;

import com.gadgetzone.entity.Usuario;
import com.gadgetzone.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UsuarioResponseDTO> listar() {
        return usuarioService.listar().stream()
                .map(u -> new UsuarioResponseDTO(u.getId(), u.getNombre()))
                .toList();
    }

    @DeleteMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> eliminarMiCuenta(Authentication authentication) {
        var usuario = (Usuario) authentication.getPrincipal();
        usuarioService.eliminarPorId(usuario.getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> actualizarDatosPersonales(@RequestBody UpdateUserRequestDTO request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String usernameActual = auth.getName();

        Usuario actualizado = usuarioService.actualizarDatos(usernameActual, request);

        UsuarioResponseDTO response = UsuarioResponseDTO.builder()
                .id(actualizado.getId())
                .nombre(actualizado.getNombre())
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/me/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> actualizarPassword(
            @RequestBody ActualizarPasswordDTO dto,
            Authentication authentication
    ) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        usuarioService.actualizarPassword(usuario.getId(), dto.getPasswordActual(), dto.getNuevaPassword());
        return ResponseEntity.noContent().build();
    }


}

