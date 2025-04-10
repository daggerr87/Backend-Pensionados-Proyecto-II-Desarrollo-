package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TRABAJO")
@Getter @Setter
public class Trabajo {
    @EmbeddedId
    private TrabajoId id;

    @Column(name = "diasDeServicio", nullable = false)
    private Long diasDeServicio;

    @ManyToOne
    @MapsId("numeroIdPersona")
    @JoinColumn (name = "numeroIdPersona")
    private Pensionado pensionado;

    @ManyToOne
    @MapsId ("nitEntidad")
    @JoinColumn (name = "nitEntidad")
    private Entidad entidad;
}

@Embeddable
@Getter @Setter
class TrabajoId implements Serializable{
    private Long nitEntidad;
    private Long numeroIdPersona;
}
