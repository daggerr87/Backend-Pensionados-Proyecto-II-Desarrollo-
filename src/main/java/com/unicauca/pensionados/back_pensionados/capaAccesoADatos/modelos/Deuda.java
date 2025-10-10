package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "DEUDA")
public class Deuda {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDeuda", nullable = false, unique = true)
    private Long idDeuda;

    @Column(name = "tipoDeuda", nullable = false)
    private TipoDeuda tipoDeuda;

    @Column(name = "estadoDeuda", nullable = false)
    private EstadoDeuda estadoDeuda;

    @OneToOne
    @Column(name = "idPersona")
    private Persona persona;

    @OneToOne
    @Column(name = "idPensionado")
    private Pensionado pensionado;

    @Column(name = "montoDeuda", nullable = false)
    private Double montoDeuda;

    @Column(name = "fechaCreacion", nullable = false)
    private String fechaCreacion = java.time.LocalDate.now().toString();

    @Column(name = "fechaVencimiento", nullable = false)
    private String fechaVencimiento;

    @Column(name = "tasaInteresAplicada", nullable = false)
    private Double tasaInteresAplicada;

    @Column(name = "usuarioRegistro", nullable = false)
    private String usuarioRegistro;

    public enum TipoDeuda {
        ACTIVO,
        PASIVO
    }
    public enum EstadoDeuda {
        VIGENTE,
        PRESCRITA,
        LITIGIO,
        PAGA,
        POR_PAGAR
    }

}



