package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import java.math.BigDecimal;

public class EntidadValorCuotaParteDTO {
    private String nit;
    private String nombre;
    private BigDecimal valorACobrar;

    public EntidadValorCuotaParteDTO() {}

    public EntidadValorCuotaParteDTO(String nit, String nombre, BigDecimal valorACobrar) {
        this.nit = nit;
        this.nombre = nombre;
        this.valorACobrar = valorACobrar;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getValorACobrar() {
        return valorACobrar;
    }

    public void setValorACobrar(BigDecimal valorACobrar) {
        this.valorACobrar = valorACobrar;
    }
}