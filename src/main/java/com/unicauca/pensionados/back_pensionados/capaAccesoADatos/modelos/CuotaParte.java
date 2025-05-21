package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
    @JoinColumn(name = "idTrabajo")
    private Trabajo trabajo;

    @Column (name = "valorCuotaParte", nullable = false,precision = 19, scale = 2 )
    private BigDecimal valorCuotaParte;

    @Column (name = "porcentajeCuotaParte", nullable = false, precision = 5, scale = 2)
    private BigDecimal porcentajeCuotaParte;

    @Column (name = "fechaGeneracion", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date fechaGeneracion;

    @Column (name = "notas", nullable = false, length = 200)
    private String notas;

    @Column (name = "cuotaParteTotal", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorTotalCuotaParte;	

    @OneToMany(mappedBy = "cuotaParte", cascade = CascadeType.ALL)
    private List<Periodo> peridos;
    
}
