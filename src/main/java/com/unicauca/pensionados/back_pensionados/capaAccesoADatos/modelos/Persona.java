package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table (name = "PERSONA")
@Inheritance(strategy =InheritanceType.JOINED)
@Getter @Setter @NoArgsConstructor
public abstract class Persona {
    /**
     * Representa la información básica y esencial de un individuo dentro del sistema de pensiones.
     * 
     * Reglas:
     * - tipoIdPersona, estadoPersona y generoPersona se modelan como enums y se persisten como INT (ORDINAL).
     * - Las fechas se manejan como LocalDate y se esperan en formato ISO-8601 (yyyy-MM-dd).
     */
    @Id
    @Column(name = "numeroIdPersona")
    private Long numeroIdPersona;
    
    /** Tipo de identificación de la persona (almacenado como entero en BD). */
    @Column (name =  "tipoIdPersona", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private TipoIdPersona tipoIdPersona;

    @Column (name = "nombrePersona", nullable = false, length = 50)
    private String nombrePersona;

    @Column (name = "apellidosPersona", nullable = false, length = 50)
    private String apellidosPersona;

    @Column (name = "fechaNacimientoPersona", nullable = false)
    private LocalDate fechaNacimientoPersona;

    @Column(name = "fechaExpedicionDocumentoIdPersona", nullable = false)
    private LocalDate fechaExpedicionDocumentoIdPersona;


    /** Estado de la persona (almacenado como entero en BD). */
    @Column (name = "estadoPersona", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private EstadoPersona estadoPersona;

    /** Género de la persona (almacenado como entero en BD, opcional). */
    @Column (name = "generoPersona", nullable = true)
    @Enumerated(EnumType.ORDINAL)
    private GeneroPersona generoPersona;


    @Column (name = "fechaDefuncionPersona")
    private LocalDate fechaDefuncionPersona;


}