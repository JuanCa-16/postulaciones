package com.postulaciones.postulaciones.nota;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import com.postulaciones.postulaciones.postulacion.Postulacion;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "nota")
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reclutador;

    private String salario;

    private String ubicacion;

    private String horario;

    private String tipoContrato;

    @Column(columnDefinition = "TEXT")
    private String otros;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postulacion_id", nullable = false, unique = true)
    private Postulacion postulacion;
}
