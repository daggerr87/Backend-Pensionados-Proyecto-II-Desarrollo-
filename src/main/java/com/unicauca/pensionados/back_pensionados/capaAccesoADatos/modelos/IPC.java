package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import java.sql.Date;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "IPC")
@Getter @Setter
public class IPC {
    @Id
    @Column(name = "fechaIpc")
    private Date fechaIpc;

    @Column(name = "inflacionTotalFinMes", nullable = false)
    private Double inflacionTotalFinMes;
}
