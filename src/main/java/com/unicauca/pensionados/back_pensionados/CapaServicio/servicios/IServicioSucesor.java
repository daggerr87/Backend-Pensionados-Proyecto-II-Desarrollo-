package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroSucesorPeticion;
import java.util.List;

public interface IServicioSucesor {
    void registrarSucesor(RegistroSucesorPeticion request);
    List<RegistroSucesorPeticion> listaSucesores();
    RegistroSucesorPeticion obtenerSucesorPorId(Long numeroIdPersona);
}
