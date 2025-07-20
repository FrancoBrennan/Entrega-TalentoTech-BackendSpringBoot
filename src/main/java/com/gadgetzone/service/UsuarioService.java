package com.gadgetzone.service;

import com.gadgetzone.dto.UpdateUsuarioRequestDTO;
import com.gadgetzone.entity.Usuario;
import com.gadgetzone.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepo;

    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepo, PasswordEncoder passwordEncoder) {
        this.usuarioRepo = usuarioRepo;
        this.passwordEncoder = passwordEncoder;
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

    public Usuario actualizarDatos(Long idUsuario, UpdateUsuarioRequestDTO request) {
        Usuario usuario = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if (request.getNombre() != null) {
            usuario.setNombre(request.getNombre());
        }

        if (request.getUsername() != null && !request.getUsername().equals(usuario.getUsername())) {
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
