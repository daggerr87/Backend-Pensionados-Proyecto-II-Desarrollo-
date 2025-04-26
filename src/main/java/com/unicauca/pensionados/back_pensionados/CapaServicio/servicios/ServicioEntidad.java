package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Entidad;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.EntidadRepositorio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroEntidadPeticion;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import jakarta.transaction.Transactional;

@Service
public class ServicioEntidad implements IServicioEntidad {

    @Autowired
    private EntidadRepositorio entidadRepository;

    /**
     * Registra una nueva entidad en la base de datos junto con sus pensionados y trabajos asociados.
     * 
     * @param request los datos de la entidad, pensionados y trabajos a registrar
     * @throws RuntimeException si ya existe una entidad con el mismo NIT o nombre
     * @throws Exception si ocurre un error al registrar la entidad
     */
    @Transactional
    @Override
    public void registrarEntidad(RegistroEntidadPeticion request) {
        // Validar si ya existe una entidad con el mismo NIT
        if (entidadRepository.existsByNitEntidad(request.getNitEntidad())) {
            throw new RuntimeException("Ya existe una entidad con el NIT: " + request.getNitEntidad());
        }

        // Validar si ya existe una entidad con el mismo nombre
        if (entidadRepository.existsByNombreEntidad(request.getNombreEntidad())) {
            throw new RuntimeException("Ya existe una entidad con el nombre: " + request.getNombreEntidad());
        }

        // Crear la entidad
        Entidad entidad = new Entidad();
        entidad.setNitEntidad(request.getNitEntidad());
        entidad.setNombreEntidad(request.getNombreEntidad());
        entidad.setDireccionEntidad(request.getDireccionEntidad());
        entidad.setTelefonoEntidad(request.getTelefonoEntidad());
        entidad.setEmailEntidad(request.getEmailEntidad());
        entidad.setEstadoEntidad(request.getEstadoEntidad());

        // Guardar la entidad
        entidadRepository.save(entidad);
    }

    /**
     * Actualiza una entidad existente en la base de datos.
     * 
     * @param nid el NIT de la entidad a actualizar
     * @param entidad los nuevos datos de la entidad
     * @throws RuntimeException si no se encuentra la entidad
     * @throws Exception si ocurre un error al actualizar la entidad
     */
    @Transactional
    @Override
    public void actualizar(Long nid, RegistroEntidadPeticion entidad) {
        // Buscar la entidad por su NIT
        Entidad entidadExistente = entidadRepository.findById(nid)
                .orElseThrow(() -> new RuntimeException("No se encontró la entidad con NIT: " + nid));

        // Actualizar los campos de la entidad
        entidadExistente.setNombreEntidad(entidad.getNombreEntidad());
        entidadExistente.setDireccionEntidad(entidad.getDireccionEntidad());
        entidadExistente.setTelefonoEntidad(entidad.getTelefonoEntidad());
        entidadExistente.setEmailEntidad(entidad.getEmailEntidad());
        entidadExistente.setEstadoEntidad(entidad.getEstadoEntidad());

        // Guardar la entidad actualizada
        entidadRepository.save(entidadExistente);
    }

    /**
     * Lista todas las entidades ordenadas por NIT ascendente.
     * 
     * @return una lista de objetos Entidad
     */
    @Override
    public List<Entidad> listarTodos() {
        return entidadRepository.findAllByOrderByNitEntidadAsc();
    }

    /**
     * Busca una entidad por su NIT.
     * 
     * @param nit el NIT de la entidad a buscar
     * @return un objeto Entidad
     * @throws RuntimeException si no se encuentra la entidad
     */
    @Override
    public Entidad buscarPorNit(Long nit) {
        return entidadRepository.findById(nit)
                .orElseThrow(() -> new RuntimeException("No se encontró la entidad con NIT: " + nit));
    }

    /**
     * Busca entidades por nombre, NIT o dirección.
     * 
     * @param query el criterio de búsqueda (nombre, NIT o dirección)
     * @return una lista de objetos Entidad
     */
    @Override
    public List<Entidad> buscarEntidadesPorCriterio(String query) {
        List<Entidad> resultado = new ArrayList<>();

        try {
            Long nit = Long.parseLong(query);
            resultado.addAll(entidadRepository.findByNitEntidadIs(nit));
        } catch (NumberFormatException e) {
            // Ignorar errores de formato para búsqueda por NIT
        }

        resultado.addAll(entidadRepository.findByNombreEntidadContainingIgnoreCase(query));
        resultado.addAll(entidadRepository.findByDireccionEntidadContainingIgnoreCase(query));
        resultado.addAll(entidadRepository.findByEmailEntidadContainingIgnoreCase(query));

        // Eliminar duplicados
        return resultado.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Activa una entidad cambiando su estado a "Activa".
     * 
     * @param nid el NIT de la entidad a activar
     * @return true si la entidad fue activada, false si no existe
     */
    @Override
    public boolean activarEntidad(Long nid) {
        Optional<Entidad> entidadOptional = entidadRepository.findById(nid);

        if (entidadOptional.isPresent()) {
            Entidad entidad = entidadOptional.get();
            entidad.setEstadoEntidad("Activa");
            entidadRepository.save(entidad);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Desactiva una entidad cambiando su estado a "No Activa".
     * 
     * @param nid el NIT de la entidad a desactivar
     * @return true si la entidad fue desactivada, false si no existe
     */
    @Override
    public boolean desactivarEntidad(Long nid) {
        Optional<Entidad> entidadOptional = entidadRepository.findById(nid);

        if (entidadOptional.isPresent()) {
            Entidad entidad = entidadOptional.get();
            entidad.setEstadoEntidad("No Activa");
            entidadRepository.save(entidad);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Busca entidades por nombre.
     * 
     * @param nombre el nombre de la entidad a buscar
     * @return una lista de objetos Entidad
     */
    @Override
    public List<Entidad> buscarEntidadPorNombre(String nombre) {
        return entidadRepository.findByNombreEntidadContainingIgnoreCase(nombre);
    }    
}