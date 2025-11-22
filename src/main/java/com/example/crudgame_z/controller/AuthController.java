package com.example.crudgame_z.controller;


import com.example.crudgame_z.dto.auth.LoginRequest;
import com.example.crudgame_z.dto.auth.RegisterRequest;
import com.example.crudgame_z.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

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