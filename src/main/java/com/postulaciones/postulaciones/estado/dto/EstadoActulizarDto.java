package com.postulaciones.postulaciones.estado.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstadoActulizarDto {
    private String nombre;
    private String color;
    private Boolean porDefecto; // B mayuscula permite null asi que si es con get, es is se se usa boolean que es solo true o false obliga a enviarlo
}
