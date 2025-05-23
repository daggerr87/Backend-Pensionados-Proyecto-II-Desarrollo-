package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import org.springframework.stereotype.Service;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Pensionado;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Trabajo;

@Service
public interface ICuotaParteServicio {
    void registrarCuotaParte (Trabajo trabajo);
    void actualizarCuotaParte(Trabajo trabajo);
    void recalcularCuotasPartesPorPensionado(Pensionado pensionado);
}