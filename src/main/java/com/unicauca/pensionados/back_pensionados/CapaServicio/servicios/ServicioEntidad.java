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
     * @return mensaje de éxito
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
     * @return mensaje de éxito
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
     * Lista todas las entidades ordenadas por NIT ascendente, incluyendo sus relaciones con pensionados y trabajos.
     * 
     * @return una lista de objetos RegistroEntidadPeticion
     */
    @Override
    public List<RegistroEntidadPeticion> listarTodos() {
        List<Entidad> entidades = entidadRepository.findAllByOrderByNitEntidadAsc();
        return entidades.stream()
                .map(this::convertirARegistroEntidadPeticion)
                .collect(Collectors.toList());
    }

    /**
     * Busca una entidad por su NIT, incluyendo sus relaciones con pensionados y trabajos.
     * 
     * @param nit el NIT de la entidad a buscar
     * @return un objeto RegistroEntidadPeticion
     */
    @Override
    public RegistroEntidadPeticion buscarPorNit(Long nit) {
        Entidad entidad = entidadRepository.findById(nit)
                .orElseThrow(() -> new RuntimeException("No se encontró la entidad con NIT: " + nit));
        return convertirARegistroEntidadPeticion(entidad);
    }

    /**
     * Busca entidades por nombre, NIT o dirección, incluyendo sus relaciones con pensionados y trabajos.
     * 
     * @param query el criterio de búsqueda (nombre, NIT o dirección)
     * @return una lista de objetos RegistroEntidadPeticion
     */
    @Override
    public List<RegistroEntidadPeticion> buscarEntidadesPorCriterio(String query) {
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

        // Eliminar duplicados y convertir a RegistroEntidadPeticion
        return resultado.stream()
                .distinct()
                .map(this::convertirARegistroEntidadPeticion)
                .collect(Collectors.toList());
    }

    /**
     * Activa una entidad cambiando su estado a "Activa".
     * 
     * @param nid el NIT de la entidad a activar
     * @return un Optional con el objeto RegistroEntidadPeticion
     */
    @Override
    public Optional<RegistroEntidadPeticion> activarEntidad(Long nid) {
        return entidadRepository.findById(nid)
                .map(entidad -> {
                    entidad.setEstadoEntidad("Activa");
                    return convertirARegistroEntidadPeticion(entidadRepository.save(entidad));
                });
    }

    /**
     * Desactiva una entidad cambiando su estado a "No Activa".
     * 
     * @param nid el NIT de la entidad a desactivar
     * @return un Optional con el objeto RegistroEntidadPeticion
     */
    @Override
    public Optional<RegistroEntidadPeticion> desactivarEntidad(Long nid) {
        return entidadRepository.findById(nid)
                .map(entidad -> {
                    entidad.setEstadoEntidad("No Activa");
                    return convertirARegistroEntidadPeticion(entidadRepository.save(entidad));
                });
    }

    /**
     * Busca entidades por nombre, incluyendo sus relaciones con pensionados y trabajos.
     * 
     * @param nombre el nombre de la entidad a buscar
     * @return una lista de objetos RegistroEntidadPeticion
     */
    @Override
    public List<RegistroEntidadPeticion> buscarEntidadPorNombre(String nombre) {
        List<Entidad> entidades = entidadRepository.findByNombreEntidadContainingIgnoreCase(nombre);
        return entidades.stream()
                .map(this::convertirARegistroEntidadPeticion)
                .collect(Collectors.toList());
    }

    /**
     * Método auxiliar para convertir una entidad a un objeto RegistroEntidadPeticion.
     * 
     * @param entidad la entidad a convertir
     * @return un objeto RegistroEntidadPeticion
     */
    private RegistroEntidadPeticion convertirARegistroEntidadPeticion(Entidad entidad) {
        RegistroEntidadPeticion registro = new RegistroEntidadPeticion();
        registro.setNitEntidad(entidad.getNitEntidad());
        registro.setNombreEntidad(entidad.getNombreEntidad());
        registro.setDireccionEntidad(entidad.getDireccionEntidad());
        registro.setTelefonoEntidad(entidad.getTelefonoEntidad());
        registro.setEmailEntidad(entidad.getEmailEntidad());
        registro.setEstadoEntidad(entidad.getEstadoEntidad());
        return registro;
    }
}