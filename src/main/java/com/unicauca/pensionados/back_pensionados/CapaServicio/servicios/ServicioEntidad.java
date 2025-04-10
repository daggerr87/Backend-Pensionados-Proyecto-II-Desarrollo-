package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Entidad;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.EntidadRepositorio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioEntidad {
    @Autowired
    private EntidadRepositorio entidadRepository;

    public Entidad registrarEntidad(Entidad entidad) {
        return entidadRepository.save(entidad);
    }

    public List<Entidad> listarTodos() {
        return entidadRepository.findAllByOrderByNitEntidadAsc();
    }

    public Entidad actualizar(Long nid, Entidad entidad) {
        if (entidadRepository.existsById(nid)) {
            entidad.setNitEntidad(nid);
            return entidadRepository.save(entidad);
        }
        return null;
    }

    public List<Entidad> buscarEntidadesPorCriterio(String query) {
        List<Entidad> resultado = new ArrayList<>();

        try {
            Long nit = Long.parseLong(query);
            resultado.addAll(entidadRepository.findByNitEntidadIs(nit));
        } catch (NumberFormatException e) {
            // no es un número, se ignora para búsqueda por NIT
        }

        resultado.addAll(entidadRepository.findByNombreEntidadContainingIgnoreCase(query));
        resultado.addAll(entidadRepository.findByDireccionEntidadContainingIgnoreCase(query));
        resultado.addAll(entidadRepository.findByEmailEntidadContainingIgnoreCase(query));

        // eliminar duplicados si alguna coincidencia se repite
        return resultado.stream().distinct().toList();
    }

    public Optional<Entidad> activarEntidad(Long nid) {
        return entidadRepository.findById(nid)
                .map(entidad -> {
                    entidad.setEstadoEntidad("Activa");
                    return entidadRepository.save(entidad);
                });
    }

    public Optional<Entidad> desactivarEntidad(Long nid) {
        return entidadRepository.findById(nid)
                .map(entidad -> {
                    entidad.setEstadoEntidad("No Activa");
                    return entidadRepository.save(entidad);
                });
    }
}
