package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Entidad;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroEntidadPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroTrabajoPeticion;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface IServicioEntidad {
    List<Entidad> buscarEntidadesPorCriterio(String query);
    List<Entidad> buscarEntidadPorNombre(String nombre);
    Entidad buscarPorNit(Long nit);
    List<Entidad> listarTodos();
    void registrarEntidad(RegistroEntidadPeticion request);
    void actualizar(Long nid, RegistroEntidadPeticion entidad);
    boolean activarEntidad(Long nid);
    boolean desactivarEntidad(Long nid);
    void editarPensionadosDeEntidad(Long nitEntidad, List<RegistroTrabajoPeticion> trabajosActualizados);
}