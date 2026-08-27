package com.postulaciones.postulaciones.estado;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.postulaciones.postulaciones.common.dto.RespuestaDto;
import com.postulaciones.postulaciones.estado.dto.EstadoActulizarDto;
import com.postulaciones.postulaciones.estado.dto.EstadoDto;
import com.postulaciones.postulaciones.estado.dto.EstadoResponseDto;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/api/estados")
@RequiredArgsConstructor
public class EstadoController {
        private final EstadoService estadoService;

        @GetMapping()
        public ResponseEntity<RespuestaDto<List<EstadoResponseDto>>> consutar() {
                List<EstadoResponseDto> estados = estadoService.consultar();
                RespuestaDto<List<EstadoResponseDto>> respuesta = new RespuestaDto<>(
                                false,
                                "Estados encontrados",
                                estados);

                return ResponseEntity.ok(respuesta);
        }

        @PostMapping()
        public ResponseEntity<RespuestaDto<Void>> crear(@Valid @RequestBody EstadoDto dto) {

                estadoService.crear(dto);

                RespuestaDto<Void> respuesta = new RespuestaDto<>(
                                false,
                                "Estado creado correctamente",
                                null);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(respuesta);
        }

        @PatchMapping("/{id}")
        public ResponseEntity<RespuestaDto<EstadoResponseDto>> actualizar(@PathVariable Long id,
                        @RequestBody EstadoActulizarDto dto) {

                EstadoResponseDto estado = estadoService.actualizar(id, dto);

                RespuestaDto<EstadoResponseDto> respuesta = new RespuestaDto<>(
                                false,
                                "Estado actualizado correctamente",
                                estado);

                return ResponseEntity.ok(respuesta);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<RespuestaDto<Void>> eliminar(@PathVariable Long id) {
                estadoService.eliminar(id);

                RespuestaDto<Void> respuesta = new RespuestaDto<Void>(
                                false,
                                "Estado eliminado correctamente",
                                null);

                return ResponseEntity.ok(respuesta);
        }

}
