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
// Servicio para la autenticación (registro e inicio de sesión)
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

    //REGISTRO
    public Map<String, Object> register(String name, String email, String password) {
        // Verifica si el email ya está registrado

        if (userRepository.existsByEmail(email)) {
                // Lanza una excepción de conflicto si el email ya existe
            throw new ConflictException("El email ya está registrado.");
        }

        // Crear nuevo usuario
        User user = new User(
                email,
                passwordEncoder.encode(password),
                name,
                Role.ROLE_USER
        );

        // Guardar usuario en la base de datos
        userRepository.save(user);

        // Generar token JWT para el nuevo usuario
        String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail(), user.getRole());

        // Retornar datos del usuario y el token
        return Map.of(
                "message", "Usuario registrado con éxito",
                "userId", user.getId(),
                "email", user.getEmail(),
                "role", user.getRole().name(),
                "token", token
        );
    }

    //LOGIN

    public Map<String, Object> login(String email, String password) {

        // Verifica credenciales
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        // Buscar usuario por email
        User user = userRepository.findByEmail(email)
        // Lanza una excepción si el usuario no es encontrado
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado."));

                // Generar token JWT
        String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail(), user.getRole());

        // Retornar datos del usuario y el token
        return Map.of(
                "userId", user.getId(),
                "email", user.getEmail(),
                "role", user.getRole().name(),
                "token", token
        );
    }
}
