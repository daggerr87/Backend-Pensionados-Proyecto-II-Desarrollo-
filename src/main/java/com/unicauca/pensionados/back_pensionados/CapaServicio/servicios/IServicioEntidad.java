package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Entidad;
import java.util.List;
import java.util.Optional;

public interface IServicioEntidad {
    List<Entidad> buscarEntidadesPorCriterio(String query);
    List<Entidad> buscarEntidadPorNombre(String nombre);
    Entidad buscarPorNit(Long nit);
    List<Entidad> listarTodos();
    Entidad registrarEntidad(Entidad entidad);
    Entidad actualizar(Long nid, Entidad entidad);
    Optional<Entidad> activarEntidad(Long nid);
    Optional<Entidad> desactivarEntidad(Long nid);
}