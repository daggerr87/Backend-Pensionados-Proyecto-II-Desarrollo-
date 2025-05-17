package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Entidad;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Pensionado;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Persona;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Trabajo;
//import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Trabajo.TrabajoId;
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
        //pensionado.setFechaDefuncionPersona(request.getFechaDefuncionPersona());

        pensionado.setFechaInicioPension(request.getFechaInicioPension());
        pensionado.setValorInicialPension(request.getValorInicialPension());
        pensionado.setResolucionPension(request.getResolucionPension());
        
        Long totalDiasTrabajo = 0L;
        pensionado.setEntidadJubilacion(entidad);

        if(pensionado.getTrabajos() == null) {
            pensionado.setTrabajos(new ArrayList<>());
        }

       if (request.getTrabajos() != null && !request.getTrabajos().isEmpty()) {
        List<Trabajo> trabajos = new ArrayList<>();
        for (RegistroTrabajoPeticion registroTrabajoPeticion : request.getTrabajos()) {
            Trabajo trabajo = new Trabajo();
            trabajo.setDiasDeServicio(registroTrabajoPeticion.getDiasDeServicio());
            trabajo.setPensionado(pensionado); // Asociar el trabajo al pensionado
            trabajo.setEntidad(entidad); // Asociar el trabajo a la entidad
            totalDiasTrabajo+= registroTrabajoPeticion.getDiasDeServicio();
            trabajos.add(trabajo);
        }

        pensionado.setTotalDiasTrabajo(totalDiasTrabajo);
        pensionado.setTrabajos(trabajos);
    }
        pensionadoRepositorio.save(pensionado);
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

        // Inicializar el total de días de trabajo
        Long totalDiasTrabajo = 0L;

        // Sumar los días de servicio de los trabajos existentes
        List<Trabajo> trabajosActualizados = new ArrayList<>(pensionadoExistente.getTrabajos());
        for (Trabajo trabajoExistente : trabajosActualizados) {
            totalDiasTrabajo += trabajoExistente.getDiasDeServicio();
        }

        // Procesar los trabajos enviados en la solicitud
        if (request.getTrabajos() != null && !request.getTrabajos().isEmpty()) {
            for (RegistroTrabajoPeticion trabajoDTO : request.getTrabajos()) {
                // Buscar si ya existe un trabajo para este pensionado y entidad
                Optional<Trabajo> trabajoExistente = trabajoRepositorio.findByPensionadoAndEntidad(pensionadoExistente, entidad);

                Trabajo trabajo;
                if (trabajoExistente.isPresent()) {
                    // Si existe, actualizar los días de servicio
                    trabajo = trabajoExistente.get();
                    totalDiasTrabajo -= trabajo.getDiasDeServicio(); // Restar los días anteriores
                    trabajo.setDiasDeServicio(trabajoDTO.getDiasDeServicio());
                } else {
                    // Si no existe, crear un nuevo trabajo
                    trabajo = new Trabajo();
                    trabajo.setDiasDeServicio(trabajoDTO.getDiasDeServicio());
                    trabajo.setPensionado(pensionadoExistente);
                    trabajo.setEntidad(entidad);
                    trabajosActualizados.add(trabajo);
                }

                // Sumar los días de servicio del trabajo actualizado o nuevo
                totalDiasTrabajo += trabajo.getDiasDeServicio();
            }
        }

        // Establecer el total de días de trabajo en el pensionado
        pensionadoExistente.setTotalDiasTrabajo(totalDiasTrabajo);

        // Actualizar la lista de trabajos del pensionado
        pensionadoExistente.setTrabajos(trabajosActualizados);

        // Guardar el pensionado actualizado
        pensionadoRepositorio.save(pensionadoExistente);

        // Guardar los trabajos actualizados
        for (Trabajo trabajo : trabajosActualizados) {
            trabajoRepositorio.save(trabajo);
        }
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
    public List<Pensionado> buscarPensionadoPorId(Long id) {
        return pensionadoRepositorio.findById(id)
                .map(List::of)
                .orElseGet(ArrayList::new);
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
