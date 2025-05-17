package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import java.util.List;

import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.EditarPeriodoPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.PeriodoRespuesta;

public interface IPeriodoServicio {
    PeriodoRespuesta consultarPeriodoPorAnio(int anio);
    void editarPeriodo(EditarPeriodoPeticion peticion); 
    List<PeriodoRespuesta> listarPeriodos();   
}
