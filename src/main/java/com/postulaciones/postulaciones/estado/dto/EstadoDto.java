package com.postulaciones.postulaciones.estado.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class EstadoDto {
    @NotBlank(message = "El Nombre del estado es obligatorio")
    private String nombre;

    @NotBlank(message = "El Color del estado es obligatorio")
    private String color;

    @NotNull(message = "El Por Defecto del estado es obligatorio")
    private boolean porDefecto;
}
