package com.postulaciones.postulaciones.estado.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstadoActulizarDto {
    private String nombre;
    private String color;
    private boolean porDefecto;
}
