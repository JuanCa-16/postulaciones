package com.postulaciones.postulaciones.exception;

public class ErrorNegocioException extends RuntimeException {
    public ErrorNegocioException(String message) {
        super(message);
    }

    public ErrorNegocioException() {
        super("Ocurrió un error interno en el negocio");
    }
}
