package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroPensionadoPeticion;
import java.util.List;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Pensionado;

public interface IPensionadoServicio {
    void registrarPensionado (RegistroPensionadoPeticion request);
    void actualizarPensionado (Long id, RegistroPensionadoPeticion request);
    List<Pensionado> listarPensionados();
    Pensionado buscarPensionadoPorId(Long id);
    List<Pensionado> buscarPensionadosPorNombre(String nombre);
    List<Pensionado> buscarPensionadosPorApellido(String apellido);
    List<Pensionado> buscarPensionadosPorCriterio(String query);
}
