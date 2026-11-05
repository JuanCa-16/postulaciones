package com.postulaciones.postulaciones.estadistica.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FechaEstadisticaDTO {
    private LocalDate fecha;
    private Long cantidad;
}
