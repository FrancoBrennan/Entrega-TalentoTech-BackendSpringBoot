package com.gadgetzone.controller;

import com.gadgetzone.entity.Usuario;
import com.gadgetzone.service.UsuarioService;
import org.springframework.web.bind.annotation.*;
import com.gadgetzone.dto.UsuarioDTO;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public UsuarioDTO crear(@RequestBody Usuario usuario) {
        Usuario guardado = usuarioService.guardar(usuario);
        return new UsuarioDTO(guardado.getId(), guardado.getNombre());
    }

    @GetMapping
    public List<UsuarioDTO> listar() {
        return usuarioService.listar().stream()
                .map(u -> new UsuarioDTO(u.getId(), u.getNombre()))
                .toList();
    }
}
