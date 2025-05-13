package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PENSIONADOCUOTAPARTE")
@Getter @Setter
public class PensionadoCuotaParte {
    @EmbeddedId
    private PensionadoCuotaParteId id;

    @ManyToOne
    @MapsId("numeroIdPersona")
    @JoinColumn(name = "numeroIdPersona")
    private Pensionado pensionado;

    @ManyToOne
    @MapsId("idCuotaParte")
    @JoinColumn(name = "idCuotaParte")
    private CuotaParte cuotaParte;

    @Embeddable
    @Getter @Setter
    public static class PensionadoCuotaParteId implements java.io.Serializable {
        private Long numeroIdPersona;
        private Long idCuotaParte;
        // equals y hashCode recomendados
    }
}
