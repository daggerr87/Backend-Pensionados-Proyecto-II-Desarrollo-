package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table (name ="PENSIONADO")
@PrimaryKeyJoinColumn (name = "numeroIdPersona") //tiene la misma PK que Persona
@Setter @Getter
public class Pensionado extends Persona{
    @Column (name = "fechaInicioPension", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaInicioPension;
    
    @Column (name = "valorPension", nullable = false)
    private BigDecimal valorPension;

    @Column (name = "resolucionPension", nullable =false , length = 50)
    private String resolucionPension;
    
    @Column (name = "totalDiasTrabajo", nullable = false)
    private Long totalDiasTrabajo;

    //relacion entidad de Jubilacion
    @ManyToOne
    @JoinColumn(name = "nitEntidad", nullable = false)
    private Entidad entidadJubilacion; 


    //relacion 1 a muchos con trabajo
    @OneToMany (mappedBy = "pensionado", cascade = CascadeType.ALL)
    private List <Trabajo> trabajos;
}