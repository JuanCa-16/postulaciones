package com.postulaciones.postulaciones.estado;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoRepository extends JpaRepository<Estado, Long> {

    Optional<Estado> findByIdAndUsuarioId(Long id, Long usuarioId);

    List<Estado> findAllByIdInAndUsuarioId(Collection<Long> ids, Long usuarioId);

    List<Estado> findByUsuarioIdOrderByFechaCreacionAsc(Long usuarioId);

    Optional<Estado> findByUsuarioIdAndPorDefectoTrue(Long usuarioId);

    long countByUsuarioId(Long usuarioId);

    Optional<Estado> findFirstByUsuarioIdAndIdNot(Long usuarioId, Long id);
}
