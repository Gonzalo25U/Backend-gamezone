package com.example.crudgame_z.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
// Configuración para el codificador de contraseñas
public class PasswordEncoderConfig {
    // Crear un bean de PasswordEncoder usando BCrypt
    @Bean
    // BCrypt es un algoritmo de hashing seguro para almacenar contraseñas
    public PasswordEncoder passwordEncoder() {
        // Devuelve una instancia de BCryptPasswordEncoder
        return new BCryptPasswordEncoder();
    }

}
