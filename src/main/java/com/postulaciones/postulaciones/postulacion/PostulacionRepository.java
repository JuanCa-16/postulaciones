package com.postulaciones.postulaciones.postulacion;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostulacionRepository extends JpaRepository<Postulacion, Long> {
    List<Postulacion> findByUsuarioIdOrderByFechaPostulacionDesc(Long usuarioId);

    Optional<Postulacion> findByIdAndUsuarioId(Long id, Long usuarioId);

    boolean existsByEstadoId(Long estadoId);
}
