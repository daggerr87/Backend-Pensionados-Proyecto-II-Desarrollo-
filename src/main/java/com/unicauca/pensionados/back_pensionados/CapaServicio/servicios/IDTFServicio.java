package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.DTFDTO;

import java.util.List;

public interface IDTFServicio {

    void guardarDTF(DTFDTO dtf);
    void actualizarDTF(DTFDTO dtf);
    void eliminarDTF(Long id);

    DTFDTO obtenerDTFPorId(Long id);
    DTFDTO obtenerDTFPorMesAnio(Long mes, Long anio);

    List<DTFDTO> listarDTFs();
}
