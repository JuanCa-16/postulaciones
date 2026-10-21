package com.postulaciones.postulaciones.nota;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.postulaciones.postulaciones.exception.ErrorNegocioException;
import com.postulaciones.postulaciones.nota.dto.NotaDto;
import com.postulaciones.postulaciones.nota.dto.NotaResponseDto;
import com.postulaciones.postulaciones.postulacion.Postulacion;
import com.postulaciones.postulaciones.postulacion.PostulacionRepository;
import com.postulaciones.postulaciones.usuario.Usuario;
import com.postulaciones.postulaciones.usuario.UsuarioService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotaService {
    private final NotaRepository notaRepository;
    private final PostulacionRepository postulacionRepository;
    private final UsuarioService usuarioService;

    @Transactional(readOnly = true)
    public NotaResponseDto obtenerNota(Long id) {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

        Postulacion postulacion = postulacionRepository
                .findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new ErrorNegocioException(
                        "Postulación no encontrada"));

        return notaRepository.findByPostulacionId(postulacion.getId())
                .map(this::convertirADto)
                .orElse(null);
    }

    @Transactional
    public NotaResponseDto guardarNota(Long id, NotaDto dto) {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

        Postulacion postulacion = postulacionRepository
                .findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new ErrorNegocioException(
                        "Postulación no encontrada"));

        // Si todos los campos están vacíos, eliminar la nota si existe
        if (notaVacia(dto)) {
            notaRepository.findByPostulacionId(postulacion.getId())
                    .ifPresent(notaRepository::delete);

            postulacion.setNota(null);

            return null;
        }

        Nota nota = notaRepository.findByPostulacionId(postulacion.getId())
                .orElseGet(() -> {
                    Nota nuevaNota = new Nota();
                    nuevaNota.setPostulacion(postulacion);
                    postulacion.setNota(nuevaNota);
                    return nuevaNota;
                });

        nota.setReclutador(dto.getReclutador());
        nota.setSalario(dto.getSalario());
        nota.setUbicacion(dto.getUbicacion());
        nota.setHorario(dto.getHorario());
        nota.setTipoContrato(dto.getTipoContrato());
        nota.setOtros(dto.getOtros());

        Nota notaGuardada = notaRepository.save(nota);

        return convertirADto(notaGuardada);
    }

    private NotaResponseDto convertirADto(Nota nota) {

        return new NotaResponseDto(
                nota.getReclutador(),
                nota.getSalario(),
                nota.getUbicacion(),
                nota.getHorario(),
                nota.getTipoContrato(),
                nota.getOtros(),
                nota.getFechaActualizacion());
    }

    private boolean notaVacia(NotaDto dto) {
        return esVacio(dto.getReclutador())
                && esVacio(dto.getSalario())
                && esVacio(dto.getUbicacion())
                && esVacio(dto.getHorario())
                && esVacio(dto.getTipoContrato())
                && esVacio(dto.getOtros());
    }

    private boolean esVacio(String valor) {
        return valor == null || valor.isBlank();
    }
}
