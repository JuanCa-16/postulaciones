package com.postulaciones.postulaciones.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.postulaciones.postulaciones.common.dto.RespuestaDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(RecursoYaExisteException.class)
        public ResponseEntity<RespuestaDto<Void>> manejarRecursoYaExistente(RecursoYaExisteException ex) {

                RespuestaDto<Void> respuesta = new RespuestaDto<>(
                                true,
                                ex.getMessage(),
                                null);

                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(respuesta);
        }

        @ExceptionHandler(CredencialesInvalidasException.class)
        public ResponseEntity<RespuestaDto<Void>> manejarCredencialesInvalidas(CredencialesInvalidasException ex) {

                RespuestaDto<Void> respuesta = new RespuestaDto<>(
                                true,
                                ex.getMessage(),
                                null);

                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(respuesta);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<RespuestaDto<Void>> manejarValidacion(MethodArgumentNotValidException ex) {

                String mensaje = ex.getBindingResult()
                                .getFieldErrors()
                                .stream() // sin esto no se peude hacer map
                                .map(error -> error.getDefaultMessage())
                                .collect(Collectors.joining("; "));

                RespuestaDto<Void> respuesta = new RespuestaDto<Void>(
                                true,
                                mensaje,
                                null);

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(respuesta);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<RespuestaDto<Void>> manejarErrorGeneral(Exception ex) {

                RespuestaDto<Void> respuesta = new RespuestaDto<Void>(
                                true,
                                "Ocurrió un error interno",
                                null);

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(respuesta);
        }
}
