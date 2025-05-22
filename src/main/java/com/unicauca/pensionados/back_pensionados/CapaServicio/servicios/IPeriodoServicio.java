package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import java.util.List;

import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistrarPeriodoPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.PeriodoRespuesta;

public interface IPeriodoServicio {
    PeriodoRespuesta consultarPeriodoPorAnio(int anio);
    void editarPeriodo(Long idPeriodo ,RegistrarPeriodoPeticion peticion); 
    List<PeriodoRespuesta> listarPeriodos();   
    void registrarPeriodo(RegistrarPeriodoPeticion peticion);
    void eliminarPeriodo(Long idPeriodo);
}
