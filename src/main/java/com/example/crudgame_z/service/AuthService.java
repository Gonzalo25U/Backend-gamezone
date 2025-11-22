package com.example.crudgame_z.service;

import com.example.crudgame_z.config.JwtTokenProvider;
import com.example.crudgame_z.exception.ConflictException;
import com.example.crudgame_z.exception.NotFoundException;
import com.example.crudgame_z.model.Role;
import com.example.crudgame_z.model.User;
import com.example.crudgame_z.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // =========================================
    //           REGISTRO
    // =========================================
    public Map<String, Object> register(String name, String email, String password) {

        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("El email ya está registrado.");
        }

        User user = new User(
                email,
                passwordEncoder.encode(password),
                name,
                Role.ROLE_USER
        );

        userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail(), user.getRole());

        return Map.of(
                "message", "Usuario registrado con éxito",
                "userId", user.getId(),
                "email", user.getEmail(),
                "role", user.getRole().name(),
                "token", token
        );
    }

    // =========================================
    //               LOGIN
    // =========================================
    public Map<String, Object> login(String email, String password) {

        // Verifica credenciales
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado."));

        String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail(), user.getRole());

        return Map.of(
                "userId", user.getId(),
                "email", user.getEmail(),
                "role", user.getRole().name(),
                "token", token
        );
    }
}
