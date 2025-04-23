package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Entidad;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.EntidadRepositorio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioEntidad implements IServicioEntidad {
    @Autowired
    private EntidadRepositorio entidadRepository;

    /**
     * Registra una nueva entidad en la base de datos. Si ya existe una entidad con el mismo NIT o nombre, devuelve null.
     * @param entidad la entidad a registrar
     * @return la entidad registrada o null si ya existe
     */
    public Entidad registrarEntidad(Entidad entidad) {
        if (entidadRepository.existsByNitEntidad(entidad.getNitEntidad())) {
            // ya existe la entidad mostrar mensaje en consola
            System.out.println("Ya existe una entidad con el NIT: " + entidad.getNitEntidad() + ".");
            return null; // ya existe la entidad
        }
        if(entidadRepository.existsByNombreEntidad(entidad.getNombreEntidad())){
            // ya existe la entidad mostrar mensaje en consola
            System.out.println("Ya existe una entidad con el nombre: " + entidad.getNombreEntidad() + ".");
            return null; // ya existe la entidad
        }

        // si no existe la entidad, se guarda
        return entidadRepository.save(entidad);
    }

    /**
     * Lista todas las entidades ordenadas por NIT ascendente.
     * @return una lista de entidades
     * @throws IllegalArgumentException si la lista está vacía
     */
    public List<Entidad> listarTodos() {
        return entidadRepository.findAllByOrderByNitEntidadAsc();
    }

    /**
     * Actualiza una entidad existente. Si no existe, devuelve null.
     * @param nid el NIT de la entidad a actualizar
     * @param entidad la entidad con los nuevos datos
     * @return la entidad actualizada o null si no se encuentra
     * @throws IllegalArgumentException si el nid es null o negativo
     */
    public Entidad actualizar(Long nid, Entidad entidad) {
        if (entidadRepository.existsByNitEntidad(nid)) {
            entidad.setNitEntidad(nid);
            return entidadRepository.save(entidad);
        }
        // ya existe la entidad mostrar mensaje en consola
        System.out.println("No existe una entidad con el NIT: " + nid + ".");
        return null;
    }

    /**
     * Busca una entidad por nit. Si no encuentra nada, devuelve null.
     * @param nit el nit de la entidad a buscar
     * @return la entidad encontrada o null si no se encuentra
     * @throws IllegalArgumentException si el nit es null o negativo
     */

    public Entidad buscarPorNit(Long nit) {
        return entidadRepository.findById(nit).orElse(null);
    }

    /**
     * Busca una entidad por nombre, nit o dirección. Si no encuentra nada,
     * devuelve null.
     * @param query el nombre, nit o dirección de la entidad a buscar
     * @return la entidad encontrada o null si no se encuentra
     */
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
    
    /**
     * Activa una entidad cambiando su estado a "Activa".
     * 
     * Este método busca la entidad en la base de datos por su ID y, si la encuentra, actualiza su estado a "Activa".
     * Luego, guarda la entidad con el nuevo estado y la retorna envuelta en un {@link Optional}. Si no se encuentra la
     * entidad con el ID proporcionado, el {@link Optional} estará vacío.
     *
     * @param nid El ID de la entidad que se desea activar.
     * @return Un {@link Optional} que contiene la entidad activada si fue encontrada y guardada correctamente,
     *         o {@link Optional#empty()} si no se encontró la entidad con el ID proporcionado.
     */
    public Optional<Entidad> activarEntidad(Long nid) {
        return entidadRepository.findById(nid)
                .map(entidad -> {
                    entidad.setEstadoEntidad("Activa");
                    return entidadRepository.save(entidad);
                });
    }

    /**
     * Desactiva una entidad cambiando su estado a "No Activa".
     * 
     * Este método busca la entidad en la base de datos por su ID y, si la encuentra, actualiza su estado a "No Activa".
     * Luego, guarda la entidad con el nuevo estado y la retorna envuelta en un {@link Optional}. Si no se encuentra la
     * entidad con el ID proporcionado, el {@link Optional} estará vacío.
     *
     * @param nid El ID de la entidad que se desea desactivar.
     * @return Un {@link Optional} que contiene la entidad desactivada si fue encontrada y guardada correctamente,
     *         o {@link Optional#empty()} si no se encontró la entidad con el ID proporcionado.
     */
    public Optional<Entidad> desactivarEntidad(Long nid) {
        return entidadRepository.findById(nid)
                .map(entidad -> {
                    entidad.setEstadoEntidad("No Activa");
                    return entidadRepository.save(entidad);
                });
    }
    
    /**
     * * Busca una entidad por nombre. Si no encuentra nada, devuelve null.
     * @param nombre el nombre de la entidad a buscar
     * @return la entidad encontrada o null si no se encuentra
     * @throws IllegalArgumentException si el nombre es null o vacío  
     */
    @Override
    public List<Entidad> buscarEntidadPorNombre(String nombre) {
        return entidadRepository.findByNombreEntidadContainingIgnoreCase(nombre);
    }
}
