package com.postulaciones.postulaciones.estado.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class EstadoDto {
    @NotBlank(message = "El nombre del estado es obligatorio")
    private String nombre;

    @NotBlank(message = "El Color del estado es obligatorio")
    private String color;
}
