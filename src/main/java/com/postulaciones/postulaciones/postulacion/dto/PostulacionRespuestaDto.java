package com.postulaciones.postulaciones.postulacion.dto;

import java.time.LocalDateTime;

import com.postulaciones.postulaciones.estado.dto.EstadoResponseDto;
import com.postulaciones.postulaciones.postulacion.Modalidad;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostulacionRespuestaDto {
    private Long id;
    private String nombreOferta;
    private String nombreEmpresa;
    private String url;
    private String paginaAplicacion;
    private Modalidad modalidad;
    private LocalDateTime fecha;
    private EstadoResponseDto estado;
}
