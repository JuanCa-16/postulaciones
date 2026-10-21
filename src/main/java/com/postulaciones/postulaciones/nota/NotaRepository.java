package com.postulaciones.postulaciones.nota;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotaRepository extends JpaRepository<Nota, Long> {
    Optional<Nota> findByPostulacionId(Long postulacionId);
}
