package com.postulaciones.postulaciones.estado;

import java.util.List;

import org.springframework.stereotype.Service;

import com.postulaciones.postulaciones.estado.dto.EstadoActulizarDto;
import com.postulaciones.postulaciones.estado.dto.EstadoDto;
import com.postulaciones.postulaciones.estado.dto.EstadoResponseDto;
import com.postulaciones.postulaciones.exception.ErrorNegocioException;
import com.postulaciones.postulaciones.historial.HistorialService;
import com.postulaciones.postulaciones.postulacion.Postulacion;
import com.postulaciones.postulaciones.usuario.Usuario;
import com.postulaciones.postulaciones.usuario.UsuarioService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstadoService {

    private final EstadoRepository estadoRepository;
    private final UsuarioService usuarioService;

    public List<EstadoResponseDto> consultar() {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

        List<Estado> estados = estadoRepository.findByUsuarioId(usuario.getId());

        return estados.stream().map(this::convertirADto).toList();
    }

    public void crear(EstadoDto dto) {

        Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

        Estado estado = new Estado();
        estado.setUsuario(usuario);
        estado.setNombre(dto.getNombre());
        estado.setColor(dto.getColor());
        estado.setPorDefecto(false);

        estadoRepository.save(estado);

    }

    @Transactional
    public EstadoResponseDto actualizar(Long id, EstadoActulizarDto dto) {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

        Estado estado = estadoRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new ErrorNegocioException("Estado no encontrado"));

        if (dto.getNombre() != null && !dto.getNombre().equals(estado.getNombre())) {
            estado.setNombre(dto.getNombre());
        }

        if (dto.getColor() != null && !dto.getColor().equals(estado.getColor())) {
            estado.setColor(dto.getColor());
        }

        // Solo actuar si el cliente envió explícitamente true
        if (Boolean.TRUE.equals(dto.isPorDefecto())
                && !estado.isPorDefecto()) {

            Estado estadoActualPorDefecto = estadoRepository
                    .findByUsuarioIdAndPorDefectoTrue(usuario.getId())
                    .orElseThrow(() -> new ErrorNegocioException(
                            "No existe un estado por defecto"));

            // El anterior deja de ser el predeterminado
            estadoActualPorDefecto.setPorDefecto(false); // El transactional hace save por defecto

            // El nuevo pasa a ser el predeterminado
            estado.setPorDefecto(true);
        }

        estado = estadoRepository.save(estado);

        return convertirADto(estado);
    }

    @Transactional
    public void eliminar(Long id) {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

        Estado estado = estadoRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new ErrorNegocioException("Estado no encontrado"));

        // Debe quedar al menos un estado
        long cantidadEstados = estadoRepository.countByUsuarioId(usuario.getId());

        if (cantidadEstados <= 1) {
            throw new ErrorNegocioException(
                    "No se puede eliminar el único estado existente");
        }

        // Si se elimina el estado por defecto,
        // otro debe convertirse en el nuevo predeterminado
        if (estado.isPorDefecto()) {

            Estado nuevoPorDefecto = estadoRepository
                    .findFirstByUsuarioIdAndIdNot(usuario.getId(), id)
                    .orElseThrow(() -> new ErrorNegocioException(
                            "No existe otro estado para establecer como predeterminado"));

            nuevoPorDefecto.setPorDefecto(true);
        }

        estadoRepository.delete(estado);
    }

    private EstadoResponseDto convertirADto(Estado estado) {
        return new EstadoResponseDto(
                estado.getId(),
                estado.getNombre(),
                estado.getColor(),
                estado.isPorDefecto());
    }

}
