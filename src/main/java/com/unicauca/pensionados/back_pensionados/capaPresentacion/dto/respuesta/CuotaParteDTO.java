package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CuotaParteDTO {
    private Long idCuotaParte;
    private BigDecimal valorCuotaParte;
    private LocalDate fechaGeneracion;

    public CuotaParteDTO() {}

    public CuotaParteDTO(Long idCuotaParte, BigDecimal valorCuotaParte, LocalDate fechaGeneracion) {
        this.idCuotaParte = idCuotaParte;
        this.valorCuotaParte = valorCuotaParte;
        this.fechaGeneracion = fechaGeneracion;
    }

    public Long getIdCuotaParte() {
        return idCuotaParte;
    }

    public void setIdCuotaParte(Long idCuotaParte) {
        this.idCuotaParte = idCuotaParte;
    }

    public BigDecimal getValorCuotaParte() {
        return valorCuotaParte;
    }

    public void setValorCuotaParte(BigDecimal valorCuotaParte) {
        this.valorCuotaParte = valorCuotaParte;
    }

    public LocalDate getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDate fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }
}