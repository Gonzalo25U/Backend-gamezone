package com.example.crudgame_z.exception;

// Excepción personalizada para conflictos (código HTTP 409)
// en este caso cuando se intenta crear un recurso que ya existe
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}