package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Entidad;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Pensionado;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Trabajo;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Trabajo.TrabajoId;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.EntidadRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.PensionadoRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.PersonaRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.TrabajoRepositorio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroPensionadoPeticion;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PensionadoServicio implements IPensionadoServicio {

    private final PersonaRepositorio personaRepositorio;
    private final PensionadoRepositorio pensionadoRepositorio;
    private final EntidadRepositorio entidadRepositorio;
    private final TrabajoRepositorio trabajoRepositorio;

    public PensionadoServicio(PersonaRepositorio personaRepositorio,
                               PensionadoRepositorio pensionadoRepositorio,
                               EntidadRepositorio entidadRepositorio,
                               TrabajoRepositorio trabajoRepositorio) {
        this.personaRepositorio = personaRepositorio;
        this.pensionadoRepositorio = pensionadoRepositorio;
        this.entidadRepositorio = entidadRepositorio;
        this.trabajoRepositorio = trabajoRepositorio;
    }

    @Transactional
    @Override
    public void registrarPensionado(RegistroPensionadoPeticion request) {

        //validar si ya se encuentra registrado el usuario
        if (personaRepositorio.existsById(request.getNumeroIdPersona())) {
            throw new RuntimeException("El numero de identifiacion ya se encuentra registrado");
        }

        //validar si ya la entidad en el que el pensionado se jubilo se encuentra registrada
        Entidad entidad = entidadRepositorio.findById(request.getNitEntidad())
                .orElseThrow(() -> new RuntimeException("La Entidad en la que el pensionado se jubilo no se encuentra registrada"));

        Pensionado pensionado = new Pensionado();
        pensionado.setNumeroIdPersona(request.getNumeroIdPersona());
        pensionado.setTipoIdPersona(request.getTipoIdPersona());
        pensionado.setNombrePersona(request.getNombrePersona());
        pensionado.setApellidosPersona(request.getApellidosPersona());
        pensionado.setFechaNacimientoPersona(request.getFechaNacimientoPersona());
        pensionado.setFechaExpedicionDocumentoIdPersona(request.getFechaExpedicionDocumentoIdPersona());
        pensionado.setEstadoPersona(request.getEstadoPersona());
        pensionado.setGeneroPersona(request.getGeneroPersona());

        pensionado.setFechaInicioPension(request.getFechaInicioPension());
        pensionado.setValorPension(request.getValorPension());
        pensionado.setResolucionPension(request.getResolucionPension());
        pensionado.setTotalDiasTrabajo(request.getTotalDiasTrabajo());
        pensionado.setEntidadJubilacion(entidad);

        pensionadoRepositorio.save(pensionado);

        //Crear ID serializable para trabajo (nitEntidad, numeroIdPersona)
        TrabajoId trabajoId = new TrabajoId(request.getNitEntidad(), request.getNumeroIdPersona());

        Trabajo trabajo = new Trabajo();
        trabajo.setId(trabajoId);
        trabajo.setDiasDeServicio(request.getDiasDeServicio());
        trabajo.setPensionado(pensionado);
        trabajo.setEntidad(entidad);

        trabajoRepositorio.save(trabajo);
    }

    /**
     * Actualizar pensionado en la base de datos.
     * @param nid el NIT de la entidad a actualizar
     * @param pensionado los nuevos datos de la entidad
     * @return mensaje de éxito
     * @throws RuntimeException si no se encuentra la entidad
     * @throws Exception si ocurre un error al actualizar la entidad
     */
    @Transactional
    @Override
    public void actualizarPensionado(Long numeroIdPersona, RegistroPensionadoPeticion pensionado) {
        // Buscar la entidad por su NIT
        Pensionado pensionadoExistente = pensionadoRepositorio.findById(numeroIdPersona)
                .orElseThrow(() -> new RuntimeException("No se encontró la persona con este numero de identidad: " + numeroIdPersona));

        // Actualizar los campos de la entidad
        pensionadoExistente.setNombrePersona(pensionado.getNombrePersona());
        pensionadoExistente.setApellidosPersona(pensionado.getApellidosPersona());
        pensionadoExistente.setFechaNacimientoPersona(pensionado.getFechaNacimientoPersona());
        pensionadoExistente.setFechaExpedicionDocumentoIdPersona(pensionado.getFechaExpedicionDocumentoIdPersona());
        pensionadoExistente.setEstadoPersona(pensionado.getEstadoPersona());
        pensionadoExistente.setGeneroPersona(pensionado.getGeneroPersona());

        // Guardar la entidad actualizada
        pensionadoRepositorio.save(pensionadoExistente);
    }

    @Override
    public List<Pensionado> listarPensionados() {
        return pensionadoRepositorio.findAll();
    }

    /**
     * Busca un pensionado por su ID.
     * 
     * @param id el ID del pensionado a buscar
     * @return un objeto Pensionado
     * @throws RuntimeException si no se encuentra el pensionado
     */
    @Override
    public Pensionado buscarPensionadoPorId(Long id) {
        return pensionadoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el pensionado con el  "
                        + "Numero de Idemtificacion: " + id ));
    }

    /**
     * Busca pensionados por su nombre.
     * 
     * @param nombre el nombre del pensionado a buscar
     * @return una lista de objetos Pensionado
     */
    @Override
    public List<Pensionado> buscarPensionadosPorNombre(String nombre) {
        return pensionadoRepositorio.findByNombrePersonaContainingIgnoreCase(nombre);
    }

    /**
     * Busca pensionados por su apellido.
     * 
     * @param apellido el apellido del pensionado a buscar
     * @return una lista de objetos Pensionado
     */
    @Override
    public List<Pensionado> buscarPensionadosPorApellido(String apellido) {
        return pensionadoRepositorio.findByApellidosPersonaContainingIgnoreCase(apellido);
    }

    /**
     * Busca pensionados por un criterio de búsqueda.
     * 
     * @param query el criterio de búsqueda
     * @return una lista de objetos Pensionado
     */
    @Override
    public List<Pensionado> buscarPensionadosPorCriterio(String query) {
        if (query == null || query.trim().isEmpty()) {
            return listarPensionados();
        }
        
        query = query.trim();
        
        if (query.matches("\\d+")) {
            Long id = Long.parseLong(query);
            return pensionadoRepositorio.findById(id)
                    .map(List::of)
                    .orElseGet(ArrayList::new);
        }

        // Búsqueda por nombre o apellido
        return pensionadoRepositorio.findByNombrePersonaContainingIgnoreCaseOrApellidosPersonaContainingIgnoreCase(query, query);
    }

    /**
     * Desactiva un pensionado por su ID.
     * 
     * @param id el ID del pensionado a desactivar
     * @throws RuntimeException si no se encuentra el pensionado
     */
    @Transactional
    @Override
    public void desactivarPensionado(Long id) {
        Pensionado pensionado = pensionadoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Pensionado no encontrado con ID: " + id));
        
        pensionado.setEstadoPersona("Inactivo");
        pensionadoRepositorio.save(pensionado);
    }
}
