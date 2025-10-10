package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.CuotaAnual;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.CuotaAnualDTO;

import java.util.List;

public interface ICuotaAnualServicio {

    void guardarCuotaAnual(CuotaAnualDTO cuotaAnual);
    void actualizarCuotaAnual(CuotaAnualDTO cuotaAnual);
    void eliminarCuotaAnual(Long id);

    CuotaAnualDTO obtenerCuotaAnualPorAnio(Long ano);
    CuotaAnualDTO obtenerCuotaAnualPorId(Long id);

    List<CuotaAnualDTO> listarCuotasAnuales();


}
