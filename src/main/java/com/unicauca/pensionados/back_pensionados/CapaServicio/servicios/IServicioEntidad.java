package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Entidad;
import java.util.List;

public interface IServicioEntidad {
    Entidad buscarPorCriterio(String query);
    Entidad buscarPorNombre(String nombre);
    Entidad buscarPorNit(Long nit);
    List<Entidad> listarTodos();
    Entidad registrarEntidad(Entidad entidad);
    Entidad actualizar(Long nid, Entidad entidad);
}