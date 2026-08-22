package com.postulaciones.postulaciones.postulacion;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.postulaciones.postulaciones.common.dto.RespuestaDto;
import com.postulaciones.postulaciones.postulacion.dto.PostulacionActualizarDto;
import com.postulaciones.postulaciones.postulacion.dto.PostulacionDto;
import com.postulaciones.postulaciones.postulacion.dto.PostulacionHistorialRespuestaDto;
import com.postulaciones.postulaciones.postulacion.dto.PostulacionRespuestaDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/api/postulaciones")
@RequiredArgsConstructor
public class PostulacionController {

        private final PostulacionService postulacionService;

        @PostMapping()
        public ResponseEntity<RespuestaDto<Void>> crear(@Valid @RequestBody PostulacionDto dto) {

                postulacionService.crear(dto);

                RespuestaDto<Void> respuesta = new RespuestaDto<>(
                                false,
                                "Postulación creada correctamente",
                                null);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(respuesta);
        }

        @GetMapping()
        public ResponseEntity<RespuestaDto<List<PostulacionRespuestaDto>>> consultar() {
                List<PostulacionRespuestaDto> postulaciones = postulacionService.consultar();

                RespuestaDto<List<PostulacionRespuestaDto>> respuesta = new RespuestaDto<>(
                                false,
                                "Postulaciones encontradas",
                                postulaciones);

                return ResponseEntity.ok(respuesta);
        }

        @GetMapping("/{id}")
        public ResponseEntity<RespuestaDto<PostulacionHistorialRespuestaDto>> obtener(@PathVariable Long id) {

                PostulacionHistorialRespuestaDto postulacion = postulacionService.obtnerPostulacion(id);

                RespuestaDto<PostulacionHistorialRespuestaDto> respuesta = new RespuestaDto<PostulacionHistorialRespuestaDto>(
                                false,
                                "Postulación encontrada",
                                postulacion);

                return ResponseEntity.ok(respuesta);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<RespuestaDto<Void>> eliminar(@PathVariable Long id) {

                postulacionService.eliminar(id);
                RespuestaDto<Void> respuesta = new RespuestaDto<Void>(
                                false,
                                "Postulación eliminada correctamente",
                                null);

                return ResponseEntity.ok(respuesta);
        }

        @PatchMapping("/{id}")
        public ResponseEntity<RespuestaDto<PostulacionRespuestaDto>> actulizar(@PathVariable Long id,
                        @RequestBody PostulacionActualizarDto dto) {
                PostulacionRespuestaDto postulacion = postulacionService.actulizar(id, dto);

                RespuestaDto<PostulacionRespuestaDto> respuesta = new RespuestaDto<>(
                                false,
                                "Postulación actualizada correctamente",
                                postulacion);

                return ResponseEntity.ok(respuesta);
        }

}
