package com.example.crudgame_z.exception;

// Excepción personalizada para recursos no encontrados (código HTTP 404)
// en este caso cuando un recurso solicitado no existe
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}