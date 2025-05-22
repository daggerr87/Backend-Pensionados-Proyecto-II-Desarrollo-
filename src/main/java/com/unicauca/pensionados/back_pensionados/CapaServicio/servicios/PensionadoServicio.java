package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;
import org.springframework.stereotype.Service;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Entidad;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Pensionado;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Trabajo;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.EntidadRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.PensionadoRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.PersonaRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.TrabajoRepositorio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroPensionadoPeticion;
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

        if (personaRepositorio.existsById(request.getNumeroIdPersona())) {
            throw new RuntimeException("El número de identificación ya se encuentra registrado");
        }

        Entidad entidad = entidadRepositorio.findById(request.getNitEntidad())
                .orElseThrow(() -> new RuntimeException("La Entidad en la que el pensionado se jubiló no se encuentra registrada"));

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

        Trabajo trabajo = new Trabajo();
        trabajo.setDiasDeServicio(request.getDiasDeServicio());
        trabajo.setEntidad(entidad); 
        trabajo.setPensionado(pensionado); 

        pensionado.setTotalDiasTrabajo(request.getDiasDeServicio());

        pensionadoRepositorio.save(pensionado);

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
        Pensionado pensionadoExistente = pensionadoRepositorio.findById(numeroIdPersona)
                .orElseThrow(() -> new RuntimeException("No se encontró la persona con este número de identidad: " + numeroIdPersona));

        Entidad entidad = entidadRepositorio.findById(request.getNitEntidad())
                .orElseThrow(() -> new RuntimeException("La Entidad en la que el pensionado se jubiló no se encuentra registrada"));

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


        pensionadoRepositorio.save(pensionadoExistente);

        Optional<Trabajo> trabajoOptional = trabajoRepositorio.findByPensionadoAndEntidad(pensionadoExistente, entidad);

        Trabajo trabajo;
        if (trabajoOptional.isPresent()) {
            trabajo = trabajoOptional.get();
        } else {
            trabajo = new Trabajo();
            trabajo.setPensionado(pensionadoExistente);
            trabajo.setEntidad(entidad);
        }

        trabajo.setDiasDeServicio(request.getDiasDeServicio());

        trabajoRepositorio.save(trabajo);

        Long totalDiasTrabajo = trabajoRepositorio.findByPensionado(pensionadoExistente)
                .stream()
                .mapToLong(Trabajo::getDiasDeServicio)
                .sum();
        pensionadoExistente.setTotalDiasTrabajo(totalDiasTrabajo);
        pensionadoRepositorio.save(pensionadoExistente);
    }

    @Override
    public List<PensionadoRespuesta> listarPensionados() {
        List<Pensionado> pensionados = pensionadoRepositorio.findAll();
        List<PensionadoRespuesta> respuestas = new ArrayList<>();
        for (Pensionado pensionado : pensionados) {
            respuestas.add(PensionadoRespuesta.builder()
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
                .entidadJubilacion(pensionado.getEntidadJubilacion().getNombreEntidad())
                .totalDiasTrabajo(pensionado.getTotalDiasTrabajo())
                .diasDeServicio(pensionado.getTrabajos().stream()
                        .filter(trabajo -> trabajo.getEntidad().getNitEntidad()
                                .equals(pensionado.getEntidadJubilacion().getNitEntidad()))
                        .mapToLong(Trabajo::getDiasDeServicio)
                        .findFirst()
                        .orElse(0L))
                .trabajos(pensionado.getTrabajos().stream()
                        .map(trabajo -> TrabajoRespuesta.builder()
                                .idTrabajo(trabajo.getIdTrabajo())
                                .diasDeServicio(trabajo.getDiasDeServicio())
                                .nitEntidad(trabajo.getEntidad().getNitEntidad())
                                .numeroIdPersona(trabajo.getPensionado().getNumeroIdPersona())
                                .entidadJubilacion(trabajo.getEntidad().getNombreEntidad())
                                .build())
                        .toList())
                .build());
        }
        return respuestas;
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
            return pensionadoRepositorio.findAll();
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


    @Override
    public PensionadoRespuesta buscarPensionadoPorId(Long id) {
        Pensionado pensionado = pensionadoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el pensionado con ID: " + id));

        Long totalDiasTrabajo = pensionado.getTrabajos().stream()
                .mapToLong(Trabajo::getDiasDeServicio)
                .sum();

        Long diasDeServicioJubilacion = pensionado.getTrabajos().stream()
            .filter(trabajo -> trabajo.getEntidad().getNitEntidad()
                    .equals(pensionado.getEntidadJubilacion().getNitEntidad()))
            .mapToLong(Trabajo::getDiasDeServicio)
            .findFirst()
            .orElse(0L);

        List<TrabajoRespuesta> trabajos = pensionado.getTrabajos().stream()
                .map(trabajo -> TrabajoRespuesta.builder()
                        .idTrabajo(trabajo.getIdTrabajo())
                        .diasDeServicio(trabajo.getDiasDeServicio())
                        .nitEntidad(trabajo.getEntidad().getNitEntidad())
                        .numeroIdPersona(trabajo.getPensionado().getNumeroIdPersona())
                        .entidadJubilacion(trabajo.getEntidad().getNombreEntidad())
                        .build())
                .toList();

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
                .entidadJubilacion(pensionado.getEntidadJubilacion().getNombreEntidad())
                .totalDiasTrabajo(totalDiasTrabajo)
                .diasDeServicio(diasDeServicioJubilacion)
                .trabajos(trabajos)
                .build();
    }
}
