package com.postulaciones.postulaciones.postulacion.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.postulaciones.postulaciones.estado.dto.EstadoResponseDto;
import com.postulaciones.postulaciones.historial.dto.HistorialResponseDto;
import com.postulaciones.postulaciones.postulacion.Modalidad;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostulacionHistorialRespuestaDto extends PostulacionRespuestaDto {
    private List<HistorialResponseDto> historial;

    public PostulacionHistorialRespuestaDto(
            Long id,
            String nombreOferta,
            String nombreEmpresa,
            String url,
            String paginaAplicacion,
            Modalidad modalidad,
            LocalDateTime fecha,
            EstadoResponseDto estado,
            List<HistorialResponseDto> historial) {

        super(id, nombreOferta, nombreEmpresa, url, paginaAplicacion, modalidad, fecha, estado);
        this.historial = historial;
    }
}
