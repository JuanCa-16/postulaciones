package com.postulaciones.postulaciones.nota.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotaResponseDto {
    private String reclutador;
    private String salario;
    private String ubicacion;
    private String horario;
    private String tipoContrato;
    private String otros;
    private LocalDateTime fechaActualizacion;
}
