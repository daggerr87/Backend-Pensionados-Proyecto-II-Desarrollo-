package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springdoc.core.converters.models.MonetaryAmount;

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
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table (name ="PENSIONADO")
@PrimaryKeyJoinColumn (name = "numeroIdPersona") //tiene la misma PK que Persona
@Setter @Getter
public class Pensionado extends Persona{
    @Column (name = "fechaInicioPension", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date fechaInicioPension;
    
    @Column (name = "valorInicialPension", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorInicialPension;

    @Column (name = "resolucionPension", nullable =false , length = 50)
    private String resolucionPension;
    
    @Column (name = "totalDiasTrabajo", nullable = true)
    private Long totalDiasTrabajo;

    //relacion entidad de Jubilacion
    @JsonBackReference //rompe el ciclo infinito de serializacion al mostrar el JSON
    @ManyToOne
    @JoinColumn(name = "nitEntidad", nullable = false)
    private Entidad entidadJubilacion; 


    //relacion 1 a muchos con trabajo
    @JsonManagedReference
    @OneToMany (mappedBy = "pensionado", cascade = CascadeType.ALL)
    private List <Trabajo> trabajos;


    //Declaramos atributos de tipo JavaMoney para poder realizar calculos mas precisos
    @Transient 
    private MonetaryAmount valorInicialPensionMoney;
}