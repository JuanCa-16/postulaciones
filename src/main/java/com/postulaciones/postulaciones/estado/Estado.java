package com.postulaciones.postulaciones.estado;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.postulaciones.postulaciones.usuario.Usuario;

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
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FORANEAS

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    //

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private boolean porDefecto;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
}
