package com.postulaciones.postulaciones.postulacion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.postulaciones.postulaciones.estadistica.dto.EstadoEstadisticaDTO;
import com.postulaciones.postulaciones.estadistica.dto.FechaEstadisticaDTO;
import com.postulaciones.postulaciones.estadistica.dto.ModalidadEstadisticaDTO;
import com.postulaciones.postulaciones.estadistica.dto.RankingEstadisticaDTO;

public interface PostulacionRepository extends JpaRepository<Postulacion, Long> {
    List<Postulacion> findByUsuarioIdOrderByFechaPostulacionDesc(Long usuarioId);

    Optional<Postulacion> findByIdAndUsuarioId(Long id, Long usuarioId);

    boolean existsByEstadoId(Long estadoId);

    // ESTADISTICAS
    long countByUsuarioId(Long usuarioId);

    @Query("""
            SELECT new com.postulaciones.postulaciones.estadistica.dto.EstadoEstadisticaDTO(
                p.estado.id,
                p.estado.nombre,
                p.estado.color,
                p.estado.porDefecto,
            COUNT(p)
            )
            FROM Postulacion p
            WHERE p.usuario.id = :usuarioId
            GROUP BY p.estado.id, p.estado.nombre, p.estado.color, p.estado.porDefecto
            ORDER BY COUNT(p) DESC
            """)
    List<EstadoEstadisticaDTO> contarPorEstado(@Param("usuarioId") Long usuarioId);

    @Query("""
                SELECT new com.postulaciones.postulaciones.estadistica.dto.FechaEstadisticaDTO(
                    CAST(p.fechaPostulacion AS LocalDate),
                    COUNT(p)
                )
                FROM Postulacion p
                WHERE p.usuario.id = :usuarioId
                AND p.fechaPostulacion >= :desde
                AND p.fechaPostulacion <= :hasta
                GROUP BY CAST(p.fechaPostulacion AS LocalDate)
                ORDER BY CAST(p.fechaPostulacion AS LocalDate) ASC
            """)
    List<FechaEstadisticaDTO> contarPorFecha(
            @Param("usuarioId") Long usuarioId,
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta);

    @Query("""
                SELECT new com.postulaciones.postulaciones.estadistica.dto.ModalidadEstadisticaDTO(
                    p.modalidad,
                    COUNT(p)
                )
                FROM Postulacion p
                WHERE p.usuario.id = :usuarioId
                GROUP BY p.modalidad
                ORDER BY COUNT(p) DESC
            """)
    List<ModalidadEstadisticaDTO> contarPorModalidad(
            @Param("usuarioId") Long usuarioId);

    @Query("""
                SELECT new com.postulaciones.postulaciones.estadistica.dto.RankingEstadisticaDTO(
                    MIN(TRIM(p.nombreEmpresa)),
                    COUNT(p)
                )
                FROM Postulacion p
                WHERE p.usuario.id = :usuarioId
                AND p.nombreEmpresa IS NOT NULL
                AND TRIM(p.nombreEmpresa) <> ''
                GROUP BY LOWER(TRIM(p.nombreEmpresa))
                ORDER BY COUNT(p) DESC, LOWER(TRIM(p.nombreEmpresa)) ASC
            """)
    List<RankingEstadisticaDTO> topEmpresas(
            @Param("usuarioId") Long usuarioId,
            Pageable pageable);

    @Query("""
                SELECT new com.postulaciones.postulaciones.estadistica.dto.RankingEstadisticaDTO(
                    MIN(TRIM(p.paginaAplicacion)),
                    COUNT(p)
                )
                FROM Postulacion p
                WHERE p.usuario.id = :usuarioId
                AND p.paginaAplicacion IS NOT NULL
                AND TRIM(p.paginaAplicacion) <> ''
                GROUP BY LOWER(TRIM(p.paginaAplicacion))
                ORDER BY COUNT(p) DESC, LOWER(TRIM(p.paginaAplicacion)) ASC
            """)
    List<RankingEstadisticaDTO> topPaginasAplicacion(
            @Param("usuarioId") Long usuarioId,
            Pageable pageable);
}
