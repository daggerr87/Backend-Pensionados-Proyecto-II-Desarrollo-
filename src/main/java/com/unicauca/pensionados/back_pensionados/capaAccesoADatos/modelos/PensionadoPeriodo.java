package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PENSIONADO_PERIODO")
@Getter
@Setter
public class PensionadoPeriodo {
    
    @EmbeddedId
    private PensionadoPeriodoId id;

    @JsonBackReference
    @ManyToOne
    @MapsId("numeroIdPersona")
    @JoinColumn(name = "numeroIdPersona", referencedColumnName = "numeroIdPersona")
    private Pensionado pensionado;

    @JsonBackReference
    @ManyToOne
    @MapsId("idPeriodo")
    @JoinColumn(name = "idPeriodo", referencedColumnName = "idPeriodo")
    private Periodo periodo;

    public PensionadoPeriodo(){
    }

    public PensionadoPeriodo(PensionadoPeriodoId id, Pensionado pensionado, Periodo periodo) {
        this.id = id;
        this.pensionado = pensionado;
        this.periodo = periodo;
    }

    @Embeddable
    @Getter
    @Setter
    public static class PensionadoPeriodoId {
        private Long numeroIdPersona;
        private Long periodo;

        public PensionadoPeriodoId() {
        }

        public PensionadoPeriodoId(Long numeroIdPersona, Long periodo) {
            this.numeroIdPersona = numeroIdPersona;
            this.periodo = periodo;
        }
    }
}   
