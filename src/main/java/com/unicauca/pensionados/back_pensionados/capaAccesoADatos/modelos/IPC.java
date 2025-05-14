package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table (name = "IPC")
@Getter @Setter
public class IPC {
    @Id
    @Column (name = "fechaIPC")
    private Long fechaIPC;
    
    @Column (name = "valorIPC", nullable = false)
    private BigDecimal valorIPC;
}
