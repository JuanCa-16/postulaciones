package com.postulaciones.postulaciones.estado.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EstadoResponseDto {
    private Long id;
    private String nombre;
    private String color;
    private Boolean porDefecto;
}
