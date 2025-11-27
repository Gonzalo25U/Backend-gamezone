package com.example.crudgame_z.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// Configuración de Swagger/OpenAPI para la documentación de la API
public class SwaggerConfig {

    @Bean
    // Definir la información básica de la API
    public OpenAPI apiInfo() {
        // Configurar el título, descripción y versión de la API
        return new OpenAPI()
                // Información de la API
                .info(new Info()
                        .title("API - CRUD Game Zone")
                        .description("Documentación de la API para el e-commerce de videojuegos")
                        .version("1.0.0"));
    }
}