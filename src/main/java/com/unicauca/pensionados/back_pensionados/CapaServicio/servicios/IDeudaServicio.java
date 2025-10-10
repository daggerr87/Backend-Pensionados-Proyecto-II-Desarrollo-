package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.DeudaDTO;

import java.util.List;

public interface IDeudaServicio {

    void guardarDeuda(DeudaDTO deuda);
    void actualizarDeuda(DeudaDTO deuda);
    void eliminarDeuda(Long id);

    DeudaDTO obtenerDeudaPorId(Long id);
    List<DeudaDTO> listarDeudas();

}
