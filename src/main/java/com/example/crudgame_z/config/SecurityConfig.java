package com.example.crudgame_z.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ← HABILITAMOS CORS
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // --- AUTORIZACIONES ---
                        .requestMatchers("/auth/**").permitAll()

                        // Solo admin puede usar endpoints críticos
                        .requestMatchers("/products/**").hasRole("ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Todos pueden ver productos
                        .requestMatchers("/products", "/products/*").permitAll()

                        // PERMITIR SOLO MIENTRAS CREAS EL ADMIN
                        .requestMatchers("/setup/create-admin").permitAll()

                        // Swagger
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // Cualquier otra ruta requiere estar logueado
                        .anyRequest().authenticated()

                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 🟦 CONFIGURACIÓN DE CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // ORÍGENES PERMITIDOS
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",          // FRONT-END LOCAL
                "https://gamezone-frontend.onrender.com" // EJEMPLO: tu sitio en Render
        ));

        // MÉTODOS PERMITIDOS
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));

        // HEADERS PERMITIDOS
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        // PERMITE TOKEN / COOKIES
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
