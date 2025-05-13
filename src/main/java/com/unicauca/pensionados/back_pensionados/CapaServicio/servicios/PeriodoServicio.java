package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.PeriodoRepositorio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.PeriodoRespuesta;

@Service
public class PeriodoServicio implements IPeriodoServicio {

    @Autowired
    private PeriodoRepositorio periodoRepositorio;

    @Override
    public PeriodoRespuesta consultarPeriodoPorAnio(int anio) {
        var periodo = periodoRepositorio.findByAnio(anio)
            .orElseThrow(() -> new RuntimeException("No existe periodo para el año: " + anio));
        return PeriodoRespuesta.builder()
            .anio(anio)
            .fechaInicioPeriodo(periodo.getFechaInicioPeriodo())
            .fechaFinPeriodo(periodo.getFechaFinPeriodo())
            .ipc(periodo.getIpc() != null ? periodo.getIpc().getInflacionTotalFinMes() : null)
            .build();
    }
}
