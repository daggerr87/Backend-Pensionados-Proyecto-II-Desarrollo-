package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import java.sql.Date;
import java.util.List;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PERIODO")
@Getter @Setter
public class Periodo {
    @Id
    @Column(name = "idPeriodo")
    private Long idPeriodo;

    @Column(name = "fechaInicioPeriodo", nullable = false)
    private Date fechaInicioPeriodo;

    @Column(name = "fechaFinPeriodo", nullable = false)
    private Date fechaFinPeriodo;

    @Column(name = "numeroMesadas", nullable = false)
    private Long numeroMesadas;

    @Column(name = "valorPension", nullable = false)
    private Double valorPension;

    @Column(name = "cuotaParteMensual", nullable = false)
    private Double cuotaParteMensual;

    @Column(name = "porcentajeIncremento", nullable = false)
    private Double porcentajeIncremento;    

    // Relación con IPC (muchos periodos pueden tener el mismo IPC)
    @ManyToOne
    @JoinColumn(name = "fechaIpc", referencedColumnName = "fechaIpc")
    private IPC ipc;

    // Relacion: un periodo puede tener muchas cuotas partes
    @OneToMany(mappedBy = "periodo")
    private List<CuotaParte> cuotasParte;
}
