package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PeriodoRespuesta {
    private Long idPeriodo;
    private Integer anio; // año del IPC asociado
    private LocalDate fechaInicioPeriodo;
    private LocalDate fechaFinPeriodo;
    private Long numeroMesadas;
    private Double valorPension;
    private Double cuotaParteMensual;
    private Double cuotaParteTotalAnio;
    private Double incrementoLey476;
    private Double ipc; // valor del IPC asociado
}