package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroPensionadoPeticion;

public interface IPensionadoServicio {
    void registrarPensionado (RegistroPensionadoPeticion request);
    void actualizarPensionado (Long id, RegistroPensionadoPeticion request);
}
