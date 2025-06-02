package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EntidadCuotaParteRespuesta {
    private Long nitEntidad;
    private String nombreEntidad;
    private BigDecimal valorCuotaParteTotal;
}
