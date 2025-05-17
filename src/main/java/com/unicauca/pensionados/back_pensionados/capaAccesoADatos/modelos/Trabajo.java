package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "TRABAJO")
@Getter
@Setter
public class Trabajo {

    @Id
    @GeneratedValue
    @Column (name = "idTrabajo" , nullable = false, unique = true)
    private Long idTrabajo;

    @Column(name = "diasDeServicio", nullable = false)
    private Long diasDeServicio;

    @JsonBackReference
    @ManyToOne
    //@MapsId("numeroIdPersona")
    @JoinColumn(name = "numeroIdPersona", referencedColumnName = "numeroIdPersona")
    private Pensionado pensionado;

    @JsonBackReference(value = "entidad-trabajo")
    @ManyToOne
    //@MapsId("nitEntidad")
    @JoinColumn(name = "nitEntidad", referencedColumnName = "nitEntidad")
    private Entidad entidad;

    /*public Trabajo() {}

    public Trabajo(TrabajoId id, Long diasDeServicio, Pensionado pensionado, Entidad entidad) {
        this.id = id;
        this.diasDeServicio = diasDeServicio;
        this.pensionado = pensionado;
        this.entidad = entidad;
    }

    @Embeddable
    @Getter
    @Setter
    public static class TrabajoId implements Serializable {
        private Long nitEntidad;
        private Long numeroIdPersona;

        public TrabajoId() {}

        public TrabajoId(Long nitEntidad, Long numeroIdPersona) {
            this.nitEntidad = nitEntidad;
            this.numeroIdPersona = numeroIdPersona;
        }
    }*/
}
