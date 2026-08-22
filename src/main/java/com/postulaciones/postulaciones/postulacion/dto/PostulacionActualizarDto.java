package com.postulaciones.postulaciones.postulacion.dto;

import com.postulaciones.postulaciones.postulacion.Modalidad;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostulacionActualizarDto {
    private String nombreOferta;
    private String nombreEmpresa;
    private String url;
    private String paginaAplicacion;
    private Modalidad modalidad;
    private Long estadoId;
}
