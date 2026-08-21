package com.postulaciones.postulaciones.historial;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.postulaciones.postulaciones.postulacion.Postulacion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Historial {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //FORANEAS
    @ManyToOne
    @JoinColumn(name = "postulacion_id", nullable = false)
    private Postulacion postulacion;

    //

    @Column(nullable = false)
    private String campoActualizado;

    private String valorAntiguo;
    private String valorNuevo;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime fechaActualizacion; 
}
