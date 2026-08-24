package com.postulaciones.postulaciones.postulacion;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.postulaciones.postulaciones.estado.Estado;
import com.postulaciones.postulaciones.estado.EstadoRepository;
import com.postulaciones.postulaciones.estado.dto.EstadoResponseDto;
import com.postulaciones.postulaciones.exception.ErrorNegocioException;
import com.postulaciones.postulaciones.historial.Historial;
import com.postulaciones.postulaciones.historial.HistorialService;
import com.postulaciones.postulaciones.historial.dto.HistorialResponseDto;
import com.postulaciones.postulaciones.postulacion.dto.PostulacionActualizarDto;
import com.postulaciones.postulaciones.postulacion.dto.PostulacionDto;
import com.postulaciones.postulaciones.postulacion.dto.PostulacionHistorialRespuestaDto;
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
    private final HistorialService historialService;

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

    @Transactional
    public void crearEnLote(List<PostulacionDto> dtos) {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

        // 1. Extraer los IDs de estado sin duplicados
        Set<Long> estadoIds = dtos.stream()
                .map(PostulacionDto::getEstadoId)
                .collect(Collectors.toSet());

        // 2. Buscar todos los estados válidos del usuario en UNA sola consulta SQL
        List<Estado> estados = estadoRepository.findAllByIdInAndUsuarioId(estadoIds, usuario.getId());

        Map<Long, Estado> estadoMap = estados.stream()
                .collect(Collectors.toMap(Estado::getId, e -> e));

        // 3. Validar que todos los estados ingresados pertenezcan al usuario
        if (estados.size() != estadoIds.size()) {
            throw new ErrorNegocioException("Uno o más estados no existen o no pertenecen al usuario");
        }

        // 4. Mapear DTOs a entidades
        List<Postulacion> postulaciones = dtos.stream().map(dto -> {
            Postulacion postulacion = new Postulacion();
            postulacion.setUsuario(usuario);
            postulacion.setEstado(estadoMap.get(dto.getEstadoId()));
            postulacion.setNombreOferta(dto.getNombreOferta());
            postulacion.setNombreEmpresa(dto.getNombreEmpresa());
            postulacion.setUrl(dto.getUrl());
            postulacion.setPaginaAplicacion(dto.getPaginaAplicacion());
            postulacion.setModalidad(dto.getModalidad());
            return postulacion;
        }).collect(Collectors.toList());

        // 5. Guardar todo el lote en una sola transacción
        postulacionRepository.saveAll(postulaciones);
    }

    public List<PostulacionRespuestaDto> consultar() {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

        List<Postulacion> postulaciones = postulacionRepository
                .findByUsuarioIdOrderByFechaPostulacionDesc(usuario.getId());

        return postulaciones.stream()
                .map(this::convertirADto)
                .toList();
    }

    public PostulacionHistorialRespuestaDto obtnerPostulacion(Long id) {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

        Postulacion postulacion = postulacionRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new ErrorNegocioException("Postulación no encontrada"));

        return convertirADtoConHistorial(postulacion);
    }

    public void eliminar(Long id) {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

        Postulacion postulacion = postulacionRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new ErrorNegocioException("Postulación no encontrada"));

        postulacionRepository.delete(postulacion);
    }

    @Transactional
    public PostulacionRespuestaDto actulizar(Long id, PostulacionActualizarDto dto) {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

        Postulacion postulacion = postulacionRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new ErrorNegocioException("Postulación no encontrada"));

        if (dto.getNombreOferta() != null && !dto.getNombreOferta().equals(postulacion.getNombreOferta())) {
            String valorAntiguo = postulacion.getNombreOferta();

            postulacion.setNombreOferta(dto.getNombreOferta());

            historialService.registrarCambio(
                    postulacion,
                    "Nombre Oferta",
                    valorAntiguo,
                    dto.getNombreOferta());
        }

        if (dto.getNombreEmpresa() != null && !dto.getNombreEmpresa().equals(postulacion.getNombreEmpresa())) {
            String valorAntiguo = postulacion.getNombreEmpresa();

            postulacion.setNombreEmpresa(dto.getNombreEmpresa());

            historialService.registrarCambio(
                    postulacion,
                    "Nombre Empresa",
                    valorAntiguo,
                    dto.getNombreEmpresa());
        }

        if (dto.getUrl() != null && !dto.getUrl().equals(postulacion.getUrl())) {

            String valorAntiguo = postulacion.getUrl();

            postulacion.setUrl(dto.getUrl());

            historialService.registrarCambio(
                    postulacion,
                    "url",
                    valorAntiguo,
                    dto.getUrl());
        }

        if (dto.getPaginaAplicacion() != null && !dto.getPaginaAplicacion().equals(postulacion.getPaginaAplicacion())) {

            String valorAntiguo = postulacion.getPaginaAplicacion();

            postulacion.setPaginaAplicacion(dto.getPaginaAplicacion());

            historialService.registrarCambio(
                    postulacion,
                    "Pagina Aplicacion",
                    valorAntiguo,
                    dto.getPaginaAplicacion());
        }

        if (dto.getModalidad() != null && !dto.getModalidad().equals(postulacion.getModalidad())) {

            String valorAntiguo = postulacion.getModalidad().name();

            postulacion.setModalidad(dto.getModalidad());

            historialService.registrarCambio(
                    postulacion,
                    "Modalidad",
                    valorAntiguo,
                    dto.getModalidad().name());
        }

        if (dto.getEstadoId() != null) {

            Estado estado = estadoRepository
                    .findByIdAndUsuarioId(dto.getEstadoId(), usuario.getId())
                    .orElseThrow(() -> new ErrorNegocioException("Estado no encontrado"));

            if (!estado.getId().equals(postulacion.getEstado().getId())) {
                String valorAntiguo = postulacion.getEstado().getNombre();
                String valorNuevo = estado.getNombre();

                postulacion.setEstado(estado);

                historialService.registrarCambio(
                        postulacion,
                        "Estado",
                        valorAntiguo,
                        valorNuevo);
            }

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

    private PostulacionHistorialRespuestaDto convertirADtoConHistorial(Postulacion postulacion) {

        List<HistorialResponseDto> historial = postulacion.getHistorial()
                .stream()
                .sorted(Comparator.comparing(
                        Historial::getFechaActualizacion).reversed())
                .map(h -> new HistorialResponseDto(
                        h.getCampoActualizado(),
                        h.getValorAntiguo(),
                        h.getValorNuevo(),
                        h.getFechaActualizacion()))
                .toList();

        return new PostulacionHistorialRespuestaDto(
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
                        postulacion.getEstado().isPorDefecto()),
                historial);
    }
}
