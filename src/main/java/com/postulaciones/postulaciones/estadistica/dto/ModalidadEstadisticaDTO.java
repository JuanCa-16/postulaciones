package com.postulaciones.postulaciones.estadistica.dto;

import com.postulaciones.postulaciones.postulacion.Modalidad;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ModalidadEstadisticaDTO {

    private Modalidad modalidad;
    private Long cantidad;
}
