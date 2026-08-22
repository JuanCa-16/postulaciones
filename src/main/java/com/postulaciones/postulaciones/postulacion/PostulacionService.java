package com.postulaciones.postulaciones.postulacion;

import java.util.List;

import org.springframework.stereotype.Service;

import com.postulaciones.postulaciones.estado.Estado;
import com.postulaciones.postulaciones.estado.EstadoRepository;
import com.postulaciones.postulaciones.estado.dto.EstadoResponseDto;
import com.postulaciones.postulaciones.exception.ErrorNegocioException;
import com.postulaciones.postulaciones.postulacion.dto.PostulacionActualizarDto;
import com.postulaciones.postulaciones.postulacion.dto.PostulacionDto;
import com.postulaciones.postulaciones.postulacion.dto.PostulacionRespuestaDto;
import com.postulaciones.postulaciones.usuario.Usuario;
import com.postulaciones.postulaciones.usuario.UsuarioService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostulacionService {

    private final EstadoRepository estadoRepository;
    private final UsuarioService usuarioService;
    private final PostulacionRepository postulacionRepository;

    public void crear(PostulacionDto dto) {

        Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

        // Buscar estado pertenece al usuario
        Estado estado = estadoRepository.findByIdAndUsuarioId(dto.getEstadoId(), usuario.getId())
                .orElseThrow(() -> new ErrorNegocioException(
                        "El estado no existe o no pertenece al usuario"));

        // Crear postulación
        Postulacion postulacion = new Postulacion();

        postulacion.setUsuario(usuario);
        postulacion.setEstado(estado);
        postulacion.setNombreOferta(dto.getNombreOferta());
        postulacion.setNombreEmpresa(dto.getNombreEmpresa());
        postulacion.setUrl(dto.getUrl());
        postulacion.setPaginaAplicacion(dto.getPaginaAplicacion());
        postulacion.setModalidad(dto.getModalidad());

        postulacionRepository.save(postulacion);
    }

    public List<PostulacionRespuestaDto> consultar() {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

        List<Postulacion> postulaciones = postulacionRepository
                .findByUsuarioIdOrderByFechaPostulacionDesc(usuario.getId());

        return postulaciones.stream()
                .map(this::convertirADto)
                .toList();
    }

    public PostulacionRespuestaDto obtnerPostulacion(Long id) {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

        Postulacion postulacion = postulacionRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new ErrorNegocioException("Postulación no encontrada"));

        return convertirADto(postulacion);
    }

    public void eliminar(Long id) {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

        Postulacion postulacion = postulacionRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new ErrorNegocioException("Postulación no encontrada"));

        postulacionRepository.delete(postulacion);
    }

    public PostulacionRespuestaDto actulizar(Long id, PostulacionActualizarDto dto) {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

        Postulacion postulacion = postulacionRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new ErrorNegocioException("Postulación no encontrada"));

        if (dto.getNombreOferta() != null) {
            postulacion.setNombreOferta(dto.getNombreOferta());
        }

        if (dto.getNombreEmpresa() != null) {
            postulacion.setNombreEmpresa(dto.getNombreEmpresa());
        }

        if (dto.getUrl() != null) {
            postulacion.setUrl(dto.getUrl());
        }

        if (dto.getPaginaAplicacion() != null) {
            postulacion.setPaginaAplicacion(dto.getPaginaAplicacion());
        }

        if (dto.getModalidad() != null) {
            postulacion.setModalidad(dto.getModalidad());
        }

        if (dto.getEstadoId() != null) {

            Estado estado = estadoRepository
                    .findByIdAndUsuarioId(dto.getEstadoId(), usuario.getId())
                    .orElseThrow(() -> new ErrorNegocioException("Estado no encontrado"));

            postulacion.setEstado(estado);
        }

        postulacion = postulacionRepository.save(postulacion);

        return convertirADto(postulacion);
    }

    private PostulacionRespuestaDto convertirADto(Postulacion postulacion) {

        return new PostulacionRespuestaDto(
                postulacion.getId(),
                postulacion.getNombreOferta(),
                postulacion.getNombreEmpresa(),
                postulacion.getUrl(),
                postulacion.getPaginaAplicacion(),
                postulacion.getModalidad(),
                postulacion.getFechaPostulacion(),
                new EstadoResponseDto(
                        postulacion.getEstado().getId(),
                        postulacion.getEstado().getNombre(),
                        postulacion.getEstado().getColor(),
                        postulacion.getEstado().isPorDefecto()));
    }
}
