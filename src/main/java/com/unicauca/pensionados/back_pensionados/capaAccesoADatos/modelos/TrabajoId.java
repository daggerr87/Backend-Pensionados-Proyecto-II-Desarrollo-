package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrabajoId implements Serializable {
    @Column(name = "nitEntidad")
    private Long nitEntidad;

    @Column(name = "numeroIdPersona")
    private Long numeroIdPersona;
}