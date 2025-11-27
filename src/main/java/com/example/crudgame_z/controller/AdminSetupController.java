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

// Controlador para la configuración inicial del administrador
@RestController
@RequestMapping("/setup")
// Permite crear un usuario administrador si no existe
public class AdminSetupController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    // Crear usuario administrador
    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin() {
    
        Optional<User> adminExists = userRepository.findByEmail("admin@admin.com");
        // Verificar si el administrador ya existe
        if (adminExists.isPresent()) {
            // Retornar respuesta indicando que el admin ya existe
            return ResponseEntity.badRequest().body("El admin ya existe");
        }
        // Crear nuevo usuario administrador
        User admin = new User();
        admin.setId(UUID.randomUUID());
        admin.setEmail("admin@admin.com");
        admin.setName("Administrador");

        // setear contraseña codificada
        admin.setPassword(passwordEncoder.encode("admin1234"));

        // Asignar rol de administrador
        admin.setRole(Role.ROLE_ADMIN); 

        // Guardar el administrador en la base de datos
        userRepository.save(admin);

        // Retornar respuesta de éxito
        return ResponseEntity.ok("ADMIN creado correctamente");
    }
}