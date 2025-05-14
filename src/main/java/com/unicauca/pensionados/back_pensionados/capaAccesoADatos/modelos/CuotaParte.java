package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import java.math.BigDecimal;
import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table (name = "CUOTA_PARTE")
@Getter @Setter
public class CuotaParte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCuotaParte;

    @ManyToOne
    @JoinColumn(name = "idPeriodo", nullable = false)
    private Periodo periodo;

    @ManyToOne
    @JoinColumn(name = "nitEntidad", nullable = false)
    private Entidad entidad;

    @ManyToOne
    @JoinColumn(name = "numeroIdPersona", nullable = false)
    private Pensionado pensionado;

    @Column (name = "valorCuotaParte", nullable = false)
    private BigDecimal valorCuotaParte;

    @Column (name = "porcentajeCuotaParte", nullable = false)
    private BigDecimal porcentajeCuotaParte;

    @Column (name = "fechaGeneracion", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date fechaGeneracion;

    @Column (name = "nombreEntidad", nullable = false, length = 200)
    private String notas;

    @Column (name = "valorPension", nullable = false)
    private BigDecimal valorPension;

    @Column (name = "cuotaParteMensual", nullable = false)
    private BigDecimal cuotaParteMensual;

    @Column (name = "incrementoLey476", nullable = true)
    private BigDecimal incrementoLey476;

    @Column (name = "valorTotalCuotaParte", nullable = false)
    private BigDecimal valorTotalCuotaParte;	

}
