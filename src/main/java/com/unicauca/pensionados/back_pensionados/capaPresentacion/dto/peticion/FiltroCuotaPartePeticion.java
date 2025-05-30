package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion;

public class FiltroCuotaPartePeticion {
    private Integer anio;
    private Integer mesInicial;
    private Integer mesFinal;

    public FiltroCuotaPartePeticion() {}

    public FiltroCuotaPartePeticion(Integer anio, Integer fechaInicio, Integer fechaFin) {
        this.anio = anio;
        this.mesInicial = fechaInicio;
        this.mesFinal = fechaFin;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getFechaInicio() {
        return mesInicial;
    }

    public void setFechaInicio(Integer fechaInicio) {
        this.mesInicial = fechaInicio;
    }

    public Integer getFechaFin() {
        return mesFinal;
    }

    public void setFechaFin(Integer fechaFin) {
        this.mesFinal = fechaFin;
    }
}