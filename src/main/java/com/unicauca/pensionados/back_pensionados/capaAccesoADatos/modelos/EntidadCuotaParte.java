package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ENTIDADCUOTAPARTE")
@Getter @Setter
public class EntidadCuotaParte {
    @EmbeddedId
    private EntidadCuotaParteId id;

    @ManyToOne
    @MapsId("idCuotaParte")
    @JoinColumn(name = "idCuotaParte")
    private CuotaParte cuotaParte;

    @ManyToOne
    @MapsId("nitEntidad")
    @JoinColumn(name = "nitEntidad")
    private Entidad entidad;

    @Embeddable
    @Getter @Setter
    public static class EntidadCuotaParteId implements java.io.Serializable {
        private Long idCuotaParte;
        private Long nitEntidad;
        // equals y hashCode recomendados
    }
}
