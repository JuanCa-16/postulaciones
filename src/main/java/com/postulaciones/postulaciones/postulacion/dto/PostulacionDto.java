package com.postulaciones.postulaciones.postulacion.dto;

import com.postulaciones.postulaciones.postulacion.Modalidad;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PostulacionDto {
    
    @NotBlank(message = "El nombre de la oferta es obligatorio")
    private String nombreOferta;

    private String nombreEmpresa;
    private String url;
    private String paginaAplicacion;

    @NotNull(message = "La modalidad es obligatoria")
    private Modalidad modalidad;

    @NotNull(message = "El estado es obligatorio")
    private Long estadoId;

}
