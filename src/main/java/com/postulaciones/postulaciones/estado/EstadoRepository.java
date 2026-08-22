package com.postulaciones.postulaciones.estado;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoRepository extends JpaRepository<Estado, Long> {

    Optional<Estado> findByIdAndUsuarioId(Long id, Long usuarioId);
}
