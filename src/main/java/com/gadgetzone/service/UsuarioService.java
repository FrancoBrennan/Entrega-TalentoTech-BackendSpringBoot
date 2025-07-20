package com.gadgetzone.service;

import com.gadgetzone.entity.Usuario;
import com.gadgetzone.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public void eliminarPorId(Long id) {
        usuarioRepo.deleteById(id);
    }

    public Usuario actualizarDatos(String usernameActual, UpdateUsuarioRequestDTO request) {
        Usuario usuario = usuarioRepo.findByUsername(usernameActual)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if (request.getNombre() != null) {
            usuario.setNombre(request.getNombre());
        }

        if (request.getUsername() != null && !request.getUsername().equals(usernameActual)) {
            if (usuarioRepo.findByUsername(request.getUsername()).isPresent()) {
                throw new IllegalArgumentException("El nuevo nombre de usuario ya está en uso");
            }
            usuario.setUsername(request.getUsername());
        }

        return usuarioRepo.save(usuario);
    }

    public void actualizarPassword(Long idUsuario, String passwordActual, String nuevaPassword) {
        Usuario usuario = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(passwordActual, usuario.getPassword())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioRepo.save(usuario);
    }


}
