package com.postulaciones.postulaciones.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RespuestaDto<T> {
    private boolean error;
    private String message;
    private T data;
}
