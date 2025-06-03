package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import java.math.BigDecimal;
import java.util.List;

public class ResultadoCobroPorEntidadDTO {
    private List<EntidadCuotaParteRespuesta> entidades;
    private BigDecimal valorTotalACobrar;

    public ResultadoCobroPorEntidadDTO() {}

    public ResultadoCobroPorEntidadDTO(List<EntidadCuotaParteRespuesta> entidades, BigDecimal valorTotalACobrar) {
        this.entidades = entidades;
        this.valorTotalACobrar = valorTotalACobrar;
    }

    public List<EntidadCuotaParteRespuesta> getEntidades() {
        return entidades;
    }

    public void setEntidades(List<EntidadCuotaParteRespuesta> entidades) {
        this.entidades = entidades;
    }

    public BigDecimal getValorTotalACobrar() {
        return valorTotalACobrar;
    }

    public void setValorTotalACobrar(BigDecimal valorTotalACobrar) {
        this.valorTotalACobrar = valorTotalACobrar;
    }
}
