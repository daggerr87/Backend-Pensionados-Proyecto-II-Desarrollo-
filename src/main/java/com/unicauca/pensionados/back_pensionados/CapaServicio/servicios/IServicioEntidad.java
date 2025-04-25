package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroEntidadPeticion;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public interface IServicioEntidad {
    List<RegistroEntidadPeticion> buscarEntidadesPorCriterio(String query);
    List<RegistroEntidadPeticion> buscarEntidadPorNombre(String nombre);
    RegistroEntidadPeticion buscarPorNit(Long nit);
    List<RegistroEntidadPeticion> listarTodos();
    void registrarEntidad(RegistroEntidadPeticion request);
    void actualizar(Long nid, RegistroEntidadPeticion entidad);
    Optional<RegistroEntidadPeticion> activarEntidad(Long nid);
    Optional<RegistroEntidadPeticion> desactivarEntidad(Long nid);
}