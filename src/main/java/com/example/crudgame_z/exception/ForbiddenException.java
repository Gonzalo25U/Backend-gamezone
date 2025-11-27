package com.example.crudgame_z.exception;
// Excepción personalizada para acceso prohibido (código HTTP 403)
// en este caso cuando un usuario intenta acceder a un recurso sin los permisos necesarios
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}