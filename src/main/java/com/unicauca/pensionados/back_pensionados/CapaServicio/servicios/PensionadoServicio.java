package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Entidad;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Pensionado;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Persona;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Trabajo;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.EntidadRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.PensionadoRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.PersonaRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.TrabajoRepositorio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroPensionadoPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroTrabajoPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.PensionadoRespuesta;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.TrabajoRespuesta;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        // Validar si ya se encuentra registrado el usuario
        if (personaRepositorio.existsById(request.getNumeroIdPersona())) {
            throw new RuntimeException("El número de identificación ya se encuentra registrado");
        }

        // Validar si la entidad en la que el pensionado se jubiló está registrada
        Entidad entidad = entidadRepositorio.findById(request.getNitEntidad())
                .orElseThrow(() -> new RuntimeException("La Entidad en la que el pensionado se jubiló no se encuentra registrada"));

        // Crear el pensionado
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
        pensionado.setValorInicialPension(request.getValorInicialPension());
        pensionado.setResolucionPension(request.getResolucionPension());
        pensionado.setEntidadJubilacion(entidad);

        // Crear el trabajo asociado
        Trabajo trabajo = new Trabajo();
        trabajo.setDiasDeServicio(request.getDiasDeServicio());
        trabajo.setEntidad(entidad); // Asociar el trabajo a la entidad
        trabajo.setPensionado(pensionado); // Asociar el trabajo al pensionado

        // Establecer el total de días de trabajo en el pensionado
        pensionado.setTotalDiasTrabajo(request.getDiasDeServicio());

        // Guardar el pensionado
        pensionadoRepositorio.save(pensionado);

        // Guardar el trabajo en el repositorio
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
    public void actualizarPensionado(Long numeroIdPersona, RegistroPensionadoPeticion request) {
        // Validar si el pensionado existe
        Pensionado pensionadoExistente = pensionadoRepositorio.findById(numeroIdPersona)
                .orElseThrow(() -> new RuntimeException("No se encontró la persona con este número de identidad: " + numeroIdPersona));

        // Validar si la entidad en la que el pensionado se jubiló está registrada
        Entidad entidad = entidadRepositorio.findById(request.getNitEntidad())
                .orElseThrow(() -> new RuntimeException("La Entidad en la que el pensionado se jubiló no se encuentra registrada"));

        // Actualizar los campos del pensionado
        pensionadoExistente.setNombrePersona(request.getNombrePersona());
        pensionadoExistente.setApellidosPersona(request.getApellidosPersona());
        pensionadoExistente.setFechaNacimientoPersona(request.getFechaNacimientoPersona());
        pensionadoExistente.setFechaExpedicionDocumentoIdPersona(request.getFechaExpedicionDocumentoIdPersona());
        pensionadoExistente.setEstadoPersona(request.getEstadoPersona());
        pensionadoExistente.setGeneroPersona(request.getGeneroPersona());
        pensionadoExistente.setFechaInicioPension(request.getFechaInicioPension());
        pensionadoExistente.setValorInicialPension(request.getValorInicialPension());
        pensionadoExistente.setResolucionPension(request.getResolucionPension());
        pensionadoExistente.setEntidadJubilacion(entidad);

        // Guardar el pensionado actualizado
        pensionadoRepositorio.save(pensionadoExistente);

        // Buscar si ya existe un trabajo para este pensionado y entidad
        Optional<Trabajo> trabajoOptional = trabajoRepositorio.findByPensionadoAndEntidad(pensionadoExistente, entidad);

        Trabajo trabajo;
        if (trabajoOptional.isPresent()) {
            // Si existe, actualizar el registro existente
            trabajo = trabajoOptional.get();
        } else {
            // Si no existe, crear un nuevo registro
            trabajo = new Trabajo();
            trabajo.setPensionado(pensionadoExistente);
            trabajo.setEntidad(entidad);
        }

        // Actualizar los campos del trabajo
        trabajo.setDiasDeServicio(request.getDiasDeServicio());

        // Guardar el trabajo actualizado o nuevo
        trabajoRepositorio.save(trabajo);

        // Actualizar el total de días de trabajo del pensionado
        Long totalDiasTrabajo = trabajoRepositorio.findByPensionado(pensionadoExistente)
                .stream()
                .mapToLong(Trabajo::getDiasDeServicio)
                .sum();
        pensionadoExistente.setTotalDiasTrabajo(totalDiasTrabajo);
        pensionadoRepositorio.save(pensionadoExistente);
    }

    @Override
    public List<Pensionado> listarPensionados() {
        return pensionadoRepositorio.findAll();
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


    //Revisar el metodo de buscarPensionadoPorID no se puede devolver una lista 
    @Override
    public PensionadoRespuesta buscarPensionadoPorId(Long id) {
        // Buscar el pensionado por su ID
        Pensionado pensionado = pensionadoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el pensionado con ID: " + id));

        // Calcular el total de días de trabajo
        Long totalDiasTrabajo = pensionado.getTrabajos().stream()
                .mapToLong(Trabajo::getDiasDeServicio)
                .sum();

        // Mapear los trabajos a TrabajoRespuesta
        List<TrabajoRespuesta> trabajos = pensionado.getTrabajos().stream()
                .map(trabajo -> TrabajoRespuesta.builder()
                        .idTrabajo(trabajo.getIdTrabajo())
                        .diasDeServicio(trabajo.getDiasDeServicio())
                        .nitEntidad(trabajo.getEntidad().getNitEntidad())
                        .numeroIdPersona(trabajo.getPensionado().getNumeroIdPersona())
                        .entidadJubilacion(trabajo.getEntidad().getNombreEntidad())
                        .build())
                .toList();

        // Construir y devolver el objeto PensionadoRespuesta
        return PensionadoRespuesta.builder()
                .numeroIdPersona(pensionado.getNumeroIdPersona())
                .tipoIdPersona(pensionado.getTipoIdPersona())
                .nombrePersona(pensionado.getNombrePersona())
                .apellidosPersona(pensionado.getApellidosPersona())
                .fechaNacimientoPersona(pensionado.getFechaNacimientoPersona())
                .fechaExpedicionDocumentoIdPersona(pensionado.getFechaExpedicionDocumentoIdPersona())
                .estadoPersona(pensionado.getEstadoPersona())
                .generoPersona(pensionado.getGeneroPersona())           
                .fechaInicioPension(pensionado.getFechaInicioPension())
                .valorInicialPension(pensionado.getValorInicialPension())
                .resolucionPension(pensionado.getResolucionPension())
                .nitEntidad(pensionado.getEntidadJubilacion().getNitEntidad())
                .totalDiasTrabajo(totalDiasTrabajo)
                .diasDeServicio(pensionado.getTrabajos().stream()
                        .mapToLong(Trabajo::getDiasDeServicio)
                        .sum())
                .trabajos(trabajos)
                .build();
    }
}
