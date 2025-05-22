package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrarPeriodoPeticion {
    private Integer fechaIPC; // año del IPC asociado
    private Long idCuotaParte;
    private LocalDate fechaInicioPeriodo;
    private LocalDate fechaFinPeriodo;
    private Long numeroMesadas;
    private Double valorPension;
    private Double cuotaParteMensual;
    private Double cuotaParteTotalPeriodo;
    private Double incrementoLey476;
}
