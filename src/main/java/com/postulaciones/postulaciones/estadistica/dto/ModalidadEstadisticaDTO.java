package com.postulaciones.postulaciones.estadistica.dto;

import com.postulaciones.postulaciones.postulacion.Modalidad;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ModalidadEstadisticaDTO {

    private Modalidad modalidad;
    private Long cantidad;
    private Integer porcentaje;
}
