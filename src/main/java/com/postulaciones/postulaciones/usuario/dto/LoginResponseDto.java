package com.postulaciones.postulaciones.usuario.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private String token;
    private UsuarioResponseDto usuario;
}
