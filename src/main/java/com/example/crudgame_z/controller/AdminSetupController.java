package com.example.crudgame_z.controller;

import com.example.crudgame_z.model.Role;
import com.example.crudgame_z.model.User;
import com.example.crudgame_z.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/setup")
public class AdminSetupController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin() {

        Optional<User> adminExists = userRepository.findByEmail("admin@admin.com");
        if (adminExists.isPresent()) {
            return ResponseEntity.badRequest().body("El admin ya existe");
        }

        User admin = new User();
        admin.setId(UUID.randomUUID());
        admin.setEmail("admin@admin.com");
        admin.setName("Administrador");

        // contraseña: Admin123
        admin.setPassword(passwordEncoder.encode("admin1234"));

        admin.setRole(Role.ROLE_ADMIN); // <<--- Enum correcto


        userRepository.save(admin);

        return ResponseEntity.ok("ADMIN creado correctamente");
    }
}