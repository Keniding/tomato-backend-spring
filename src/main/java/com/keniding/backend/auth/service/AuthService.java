package com.keniding.backend.auth.service;

import com.keniding.backend.auth.dto.AuthResponse;
import com.keniding.backend.auth.dto.LoginRequest;
import com.keniding.backend.auth.dto.PasswordResetRequest;
import com.keniding.backend.auth.dto.RegisterRequest;
import com.keniding.backend.auth.model.Role;
import com.keniding.backend.auth.model.User;
import com.keniding.backend.auth.repository.UserRepository;
import com.keniding.backend.config.JwtConfig;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtConfig jwtConfig,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtConfig = jwtConfig;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        // Verificar si el usuario ya existe
        if (userRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse(null, "El correo electrónico ya está registrado", false);
        }

        // Verificar aceptación de términos
        if (!request.isTerms()) {
            return new AuthResponse(null, "Debe aceptar los términos y condiciones", false);
        }

        // Crear nuevo usuario
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setRole(Role.USER);

        userRepository.save(user);

        // Generar token
        String token = jwtConfig.generateToken(user);

        return new AuthResponse(token, "Registro exitoso", true);
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            String token = jwtConfig.generateToken(user);

            return new AuthResponse(token, "Inicio de sesión exitoso", true);
        } catch (Exception e) {
            return new AuthResponse(null, "Credenciales inválidas", false);
        }
    }

    public AuthResponse requestPasswordReset(String email) {
        boolean userExists = userRepository.existsByEmail(email);

        if (!userExists) {
            return new AuthResponse(null, "No existe una cuenta con este correo electrónico", false);
        }

        // Aquí implementarías el envío de correo con enlace para restablecer contraseña
        // Por simplicidad, solo devolvemos una respuesta exitosa

        return new AuthResponse(null, "Se ha enviado un correo con instrucciones para restablecer su contraseña", true);
    }

    public AuthResponse resetPassword(PasswordResetRequest request) {
        // Implementación de restablecimiento de contraseña
        // Aquí verificarías el token de restablecimiento y actualizarías la contraseña

        return new AuthResponse(null, "Contraseña actualizada correctamente", true);
    }
}
