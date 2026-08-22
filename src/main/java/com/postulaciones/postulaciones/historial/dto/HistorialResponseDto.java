package com.postulaciones.postulaciones.historial.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HistorialResponseDto {
    private String campoActualizado;
    private String valorAntiguo;
    private String valorNuevo;
    private LocalDateTime fechaActualizacion;
}
