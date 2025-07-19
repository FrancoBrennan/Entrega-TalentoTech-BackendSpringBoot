package com.gadgetzone.Auth;

import com.gadgetzone.Enum.Role;
import com.gadgetzone.dto.LoginRequestDTO;
import com.gadgetzone.dto.RegisterRequestDTO;
import com.gadgetzone.entity.Usuario;
import com.gadgetzone.repository.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import com.gadgetzone.jwt.JwtService;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequestDTO request) {
        Usuario user = Usuario.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .nombre(request.getNombre())
                .role(Role.USER)
                .build();
        usuarioRepository.save(user);
        String jwt = jwtService.generateToken(user);
        return AuthResponse.builder().token(jwt).build();
    }

    public AuthResponse login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        Usuario user = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + request.getUsername()));
        String jwt = jwtService.generateToken(user);
        return AuthResponse.builder().token(jwt).build();
    }

    public Usuario getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Usuario) auth.getPrincipal();
    }

}
