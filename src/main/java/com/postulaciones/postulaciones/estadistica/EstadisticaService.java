package com.postulaciones.postulaciones.estadistica;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.postulaciones.postulaciones.estadistica.dto.EstadoEstadisticaDTO;
import com.postulaciones.postulaciones.estadistica.dto.FechaEstadisticaDTO;
import com.postulaciones.postulaciones.estadistica.dto.ModalidadEstadisticaDTO;
import com.postulaciones.postulaciones.estadistica.dto.RankingEstadisticaDTO;
import com.postulaciones.postulaciones.postulacion.PostulacionRepository;
import com.postulaciones.postulaciones.usuario.Usuario;
import com.postulaciones.postulaciones.usuario.UsuarioService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstadisticaService {

    private final PostulacionRepository postulacionRepository;
    private final UsuarioService usuarioService;

    public Long totalPostulaciones() {

        Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

        return postulacionRepository.countByUsuarioId(usuario.getId());
    }

    public List<EstadoEstadisticaDTO> postulacionesPorEstado() {

        Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

        Long total = postulacionRepository.countByUsuarioId(usuario.getId());

        List<EstadoEstadisticaDTO> resultados = postulacionRepository.contarPorEstado(usuario.getId());

        resultados.forEach(estado -> {
            double porcentaje = total == 0
                    ? 0.0
                    : Math.round((estado.getCantidad() * 10000.0) / total) / 100.0;

            estado.setPorcentaje(porcentaje);
        });

        return resultados;
    }

    public List<FechaEstadisticaDTO> postulacionesDiarias() {

        Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

        LocalDate hoy = LocalDate.now(ZoneId.of("America/Bogota"));

        LocalDate desdeFecha = hoy.withDayOfYear(1);
        LocalDate hastaFecha = hoy.withDayOfYear(hoy.lengthOfYear());

        LocalDateTime desde = desdeFecha.atStartOfDay();
        LocalDateTime hasta = hastaFecha.atTime(LocalTime.MAX);

        List<FechaEstadisticaDTO> resultados = postulacionRepository.contarPorFecha(usuario.getId(), desde, hasta);

        Map<LocalDate, Long> cantidades = resultados.stream()
                .collect(Collectors.toMap(
                        FechaEstadisticaDTO::getFecha,
                        FechaEstadisticaDTO::getCantidad));

        return IntStream.rangeClosed(0, hoy.lengthOfYear() - 1)
                .mapToObj(i -> {
                    LocalDate fecha = desdeFecha.plusDays(i);

                    return new FechaEstadisticaDTO(
                            fecha,
                            cantidades.getOrDefault(fecha, 0L));
                })
                .collect(Collectors.toList());
    }

    public List<ModalidadEstadisticaDTO> postulacionesPorModalidad() {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

        Long total = postulacionRepository.countByUsuarioId(usuario.getId());

        List<ModalidadEstadisticaDTO> resultados = postulacionRepository.contarPorModalidad(usuario.getId());
        resultados.forEach(estado -> {
            int porcentaje = total == 0
                    ? 0
                    : (int) Math.round((estado.getCantidad() * 100.0) / total);

            estado.setPorcentaje(porcentaje);
        });

        return resultados;
    }

    public List<RankingEstadisticaDTO> topEmpresas(Long estadoId) {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

        return postulacionRepository.topEmpresas(
                usuario.getId(),
                estadoId,
                PageRequest.of(0, 5));
    }

    public List<RankingEstadisticaDTO> topPaginasAplicacion(Long estadoId) {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

        return postulacionRepository.topPaginasAplicacion(
                usuario.getId(),
                estadoId,
                PageRequest.of(0, 5));
    }
}
