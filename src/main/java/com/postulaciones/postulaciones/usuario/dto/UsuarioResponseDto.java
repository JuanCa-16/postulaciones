package com.postulaciones.postulaciones.usuario.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioResponseDto {

    private Long id;
    private String correo;
    private String nombre;

}
