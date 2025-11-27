package com.example.crudgame_z.service;

import com.example.crudgame_z.model.User;
import com.example.crudgame_z.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
// Servicio personalizado para cargar detalles del usuario para autenticación
public class CustomUserDetailsService implements UserDetailsService {

    // Repositorio de usuarios
    private final UserRepository userRepository;

    // Inyección de dependencias a través del constructor
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    // Cargar usuario por su email (username)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Buscar usuario en la base de datos por email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        // Convertimos ROLE_ADMIN → ADMIN (lo que exige Spring Security)
        String role = user.getRole().name().substring(5);

        // Retornar objeto UserDetails con la información del usuario
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(role)
                .build();
    }
}
