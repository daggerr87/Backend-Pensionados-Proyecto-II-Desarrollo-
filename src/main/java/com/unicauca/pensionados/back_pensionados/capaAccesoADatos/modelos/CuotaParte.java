package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CUOTA_PARTE")
@Getter @Setter
public class CuotaParte {
    @Id
    @Column(name = "idCuotaParte")
    private Long idCuotaParte;

    @Column(name = "valorCuotaParte", nullable = false)
    private Double valorCuotaParte;

    @Column(name = "porcentajeCuotaParte", nullable = false)
    private Double porcentajeCuotaParte;

    @Column(name = "valorTotalCuotaParte", nullable = false)
    private Double valorTotalCuotaParte;

    @Column(name = "fechaGeneracion", nullable = false)
    private Date fechaGeneracion;

    @Column(name = "notas")
    private String notas;

    // Relación: una cuota parte pertenece a un unico periodo
    @ManyToOne
    @JoinColumn(name = "idPeriodo")
    private Periodo periodo;

    // Relación con EntidadCuotaParte (muchas entidades pueden estar asociadas a una cuota parte)
    @OneToMany(mappedBy = "cuotaParte")
    private List<EntidadCuotaParte> entidadesCuotaParte;

    // Relación con PensionadoCuotaParte (muchos pensionados pueden estar asociados a una cuota parte)
    @OneToMany(mappedBy = "cuotaParte")
    private List<PensionadoCuotaParte> pensionadosCuotaParte;
}
