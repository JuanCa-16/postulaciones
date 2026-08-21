package com.postulaciones.postulaciones.usuario;

import java.util.ArrayList;
import java.util.List;

import com.postulaciones.postulaciones.estado.Estado;
import com.postulaciones.postulaciones.postulacion.Postulacion;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String correo; 

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String clave;

     // PADRES DE FORANEAS (No es una columna)

    @OneToMany(
        mappedBy = "usuario",
        cascade = CascadeType.ALL,
        orphanRemoval = true
     )
     private List<Postulacion> postulaciones = new ArrayList<>();
    
    @OneToMany(
        mappedBy = "usuario",
        cascade = CascadeType.ALL,
        orphanRemoval = true
     )
     private List<Estado> estados = new ArrayList<>();
    
 }
