package com.gadgetzone.service;

import com.gadgetzone.entity.Usuario;
import com.gadgetzone.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepo;

    public UsuarioService(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    public Usuario guardar(Usuario usuario) {
        return usuarioRepo.save(usuario);
    }

    public List<Usuario> listar() {
        return usuarioRepo.findAll();
    }
}
