package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import java.math.BigDecimal;
import java.util.List;

public class ResultadoCobroPorPeriodoDTO {
    private List<PensionadoConCuotaParteDTO> pensionados;
    private BigDecimal valorTotalACobrar;

    public ResultadoCobroPorPeriodoDTO() {}

    public ResultadoCobroPorPeriodoDTO(List<PensionadoConCuotaParteDTO> pensionados, BigDecimal valorTotalACobrar) {
        this.pensionados = pensionados;
        this.valorTotalACobrar = valorTotalACobrar;
    }

    public List<PensionadoConCuotaParteDTO> getPensionados() {
        return pensionados;
    }

    public void setPensionados(List<PensionadoConCuotaParteDTO> pensionados) {
        this.pensionados = pensionados;
    }

    public BigDecimal getValorTotalACobrar() {
        return valorTotalACobrar;
    }

    public void setValorTotalACobrar(BigDecimal valorTotalACobrar) {
        this.valorTotalACobrar = valorTotalACobrar;
    }
}