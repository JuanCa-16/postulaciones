package com.postulaciones.postulaciones.postulacion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.postulaciones.postulaciones.estado.Estado;
import com.postulaciones.postulaciones.historial.Historial;
import com.postulaciones.postulaciones.usuario.Usuario;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Postulacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //FORANEAS
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "estado_id", nullable = false)
    private Estado estado;  

    //

    private String nombreOferta;
    private String nombreEmpresa; 
    private String paginaAplicacion;
    private String url; 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Modalidad modalidad;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime fechaPostulacion;  


    // PADRES DE FORANEAS (No es una columna)
    @OneToMany(
        mappedBy = "postulacion", //Debe coindiir con el del objeto Historial
        cascade = CascadeType.ALL,
        orphanRemoval = true)
    private List<Historial> historial = new ArrayList<>();
    
}
