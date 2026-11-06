package com.postulaciones.postulaciones.estadistica;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.postulaciones.postulaciones.common.dto.RespuestaDto;
import com.postulaciones.postulaciones.estadistica.dto.EstadoEstadisticaDTO;
import com.postulaciones.postulaciones.estadistica.dto.FechaEstadisticaDTO;
import com.postulaciones.postulaciones.estadistica.dto.ModalidadEstadisticaDTO;
import com.postulaciones.postulaciones.estadistica.dto.RankingEstadisticaDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/estadisticas")
@RequiredArgsConstructor
public class EstadisticaController {

        private final EstadisticaService estadisticaService;

        @GetMapping("/total")
        public ResponseEntity<RespuestaDto<Long>> totalPostulaciones() {

                return ResponseEntity.ok(
                                new RespuestaDto<>(
                                                false,
                                                "Total de postulaciones obtenido correctamente",
                                                estadisticaService.totalPostulaciones()));
        }

        @GetMapping("/estados")
        public ResponseEntity<RespuestaDto<List<EstadoEstadisticaDTO>>> postulacionesPorEstado() {

                return ResponseEntity.ok(
                                new RespuestaDto<>(
                                                false,
                                                "Estadísticas por estado obtenidas correctamente",
                                                estadisticaService.postulacionesPorEstado()));
        }

        @GetMapping("/diarias")
        public ResponseEntity<RespuestaDto<List<FechaEstadisticaDTO>>> postulacionesDiarias() {

                return ResponseEntity.ok(
                                new RespuestaDto<>(
                                                false,
                                                "Estadísticas diarias obtenidas correctamente",
                                                estadisticaService.postulacionesDiarias()));
        }

        @GetMapping("/modalidades")
        public ResponseEntity<RespuestaDto<List<ModalidadEstadisticaDTO>>> postulacionesPorModalidad() {

                return ResponseEntity.ok(
                                new RespuestaDto<>(
                                                false,
                                                "Estadísticas por modalidad obtenidas correctamente",
                                                estadisticaService.postulacionesPorModalidad()));
        }

        @GetMapping("/top-empresas")
        public ResponseEntity<RespuestaDto<List<RankingEstadisticaDTO>>> topEmpresas(
                        @RequestParam(required = false) Long estadoId) {

                return ResponseEntity.ok(
                                new RespuestaDto<>(
                                                false,
                                                "Top de empresas obtenido correctamente",
                                                estadisticaService.topEmpresas(estadoId)));
        }

        @GetMapping("/top-paginas")
        public ResponseEntity<RespuestaDto<List<RankingEstadisticaDTO>>> topPaginasAplicacion(
                        @RequestParam(required = false) Long estadoId) {

                return ResponseEntity.ok(
                                new RespuestaDto<>(
                                                false,
                                                "Top de páginas de aplicación obtenido correctamente",
                                                estadisticaService.topPaginasAplicacion(estadoId)));
        }
}
