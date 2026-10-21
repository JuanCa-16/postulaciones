package com.postulaciones.postulaciones.nota;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postulaciones.postulaciones.common.dto.RespuestaDto;
import com.postulaciones.postulaciones.nota.dto.NotaDto;
import com.postulaciones.postulaciones.nota.dto.NotaResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/postulaciones/{id}/nota")
@RequiredArgsConstructor
public class NotaController {
    private final NotaService notaService;

    @GetMapping
    public ResponseEntity<RespuestaDto<NotaResponseDto>> obtener(@PathVariable Long id) {
        NotaResponseDto nota = notaService.obtenerNota(id);

        RespuestaDto<NotaResponseDto> respuesta = new RespuestaDto<NotaResponseDto>(false,
                nota == null ? "La postulación no tiene nota" : "Nota encontrada", nota);

        return ResponseEntity.ok(respuesta);
    }

    @PutMapping
    public ResponseEntity<RespuestaDto<NotaResponseDto>> guardar(
            @PathVariable Long id,
            @RequestBody NotaDto dto) {

        NotaResponseDto nota = notaService.guardarNota(id, dto);

        RespuestaDto<NotaResponseDto> respuesta = new RespuestaDto<>(
                false,
                "Nota guardada correctamente",
                nota);

        return ResponseEntity.ok(respuesta);
    }
}
