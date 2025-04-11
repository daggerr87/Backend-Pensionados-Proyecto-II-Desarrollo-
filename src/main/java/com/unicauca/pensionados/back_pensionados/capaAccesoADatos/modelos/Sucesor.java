package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="SUCESOR")
@PrimaryKeyJoinColumn (name = "numeroIdPersona")  //tiene la misma PK que Persona
@Getter @Setter

public class Sucesor extends Persona{
    @Column (name = "fechaInicioSucesion", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaInicioSucesion;
    
    @ManyToOne
    @JoinColumn(name = "numeroIdPensionado", nullable = false)
    private Pensionado pensionado;
}
