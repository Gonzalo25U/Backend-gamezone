package com.example.crudgame_z.controller;


import com.example.crudgame_z.dto.auth.LoginRequest;
import com.example.crudgame_z.dto.auth.RegisterRequest;
import com.example.crudgame_z.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
// Permitir solicitudes desde cualquier origen
@CrossOrigin(origins = "*")
// Controlador para la autenticación (registro e inicio de sesión)
public class AuthController {

    // Servicio de autenticación
    private final AuthService authService;

    // Inyección de dependencias a través del constructor
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(
                authService.register(request.name, request.email, request.password)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(
                authService.login(request.email, request.password)
        );
    }
}