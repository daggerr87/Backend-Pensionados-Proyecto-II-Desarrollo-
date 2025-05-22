package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table (name = "PERIODO")
@Getter @Setter
public class Periodo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPeriodo;

    @ManyToOne
    @JoinColumn(name = "fechaIPC", nullable = false)
    private IPC IPC;

    @ManyToOne
    @JoinColumn(name = "idCuotaParte")
    private CuotaParte cuotaParte;

    @Column (name = "fechaInicioPeriodo", nullable = false)
    private LocalDate fechaInicioPeriodo;

    @Column (name = "fechaFinPeriodo", nullable = false)
    private LocalDate fechaFinPeriodo;

    @Column (name = "numeroMesadas", nullable = false)
    private Long numeroMesadas;

    @Column (name = "valorPension", nullable = false)
    private BigDecimal valorPension;

    @Column (name = "cuotaParteMensual", nullable = false)
    private BigDecimal cuotaParteMensual;

    @Column (name = "cuotaParteTotalAnio", nullable = false)
    private BigDecimal cuotaParteTotalAnio;

    @Column (name = "incrementoAdicionalLey476", nullable = true)
    private BigDecimal incrementoLey476;
    
}
