package com.postulaciones.postulaciones.estadistica.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EstadoEstadisticaDTO {
    private Long estadoId;
    private String nombre;
    private String color;
    private Boolean porDefecto;
    private Long cantidad;
}
