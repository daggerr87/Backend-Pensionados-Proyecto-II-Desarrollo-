package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Column (name = "valorCuotaParte", nullable = false)
    private BigDecimal valorCuotaParte;

    @Column (name = "porcentajeCuotaParte", nullable = false)
    private BigDecimal porcentajeCuotaParte;

    @Column (name = "fechaGeneracion", nullable = true)
    private LocalDate fechaGeneracion;

    @Column (name = "notas", nullable = false, length = 200)
    private String notas;

    @Column (name = "cuotaParteTotal", nullable = false)
    private BigDecimal valorTotalCuotaParte;	

    @OneToMany(mappedBy = "cuotaParte", cascade = CascadeType.ALL)
    private List<Periodo> periodos;
    
}
