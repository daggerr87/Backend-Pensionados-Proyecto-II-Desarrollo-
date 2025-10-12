package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;


import org.springframework.stereotype.Service;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.CuotaParte;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Entidad;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Pensionado;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Trabajo;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.CuotaParteRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.EntidadRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.PensionadoRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.PersonaRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.TrabajoRepositorio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroPensionadoPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroTrabajoPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.EntidadCuotaParteRespuesta;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.PensionadoRespuesta;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.SucesorRespuesta;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.TrabajoRespuesta;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.enumeradores.EstadoPersona;

import jakarta.transaction.Transactional;
//import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@Service
public class PensionadoServicio implements IPensionadoServicio {

    private final PersonaRepositorio personaRepositorio;
    private final PensionadoRepositorio pensionadoRepositorio;
    private final EntidadRepositorio entidadRepositorio;
    private final TrabajoRepositorio trabajoRepositorio;
    private final CuotaParteServicio cuotaParteServicio;

    public PensionadoServicio(PersonaRepositorio personaRepositorio,
                              PensionadoRepositorio pensionadoRepositorio,
                              EntidadRepositorio entidadRepositorio,
                              TrabajoRepositorio trabajoRepositorio,
                              CuotaParteServicio cuotaParteServicio) {
        this.personaRepositorio = personaRepositorio;
        this.pensionadoRepositorio = pensionadoRepositorio;
        this.entidadRepositorio = entidadRepositorio;
        this.trabajoRepositorio = trabajoRepositorio;
        this.cuotaParteServicio = cuotaParteServicio;
    }

    @Transactional
    @Override
    public void registrarPensionado(RegistroPensionadoPeticion request) {
        if (personaRepositorio.existsByTipoIdentificacionAndNumeroIdentificacion(request.getTipoIdentificacion(), request.getNumeroIdentificacion())) {
            throw new RuntimeException("Ya existe una persona con ese tipo y número de identificación");
        }

        Entidad entidadJubilacion = entidadRepositorio.findById(request.getNitEntidad())
                .orElseThrow(() -> new RuntimeException("La Entidad de jubilación no se encuentra registrada"));

        // 1. Crear y guardar la entidad Pensionado
        Pensionado pensionado = new Pensionado();
        pensionado.setNumeroIdentificacion(request.getNumeroIdentificacion());
        pensionado.setTipoIdentificacion(request.getTipoIdentificacion());
        pensionado.setNombrePersona(request.getNombrePersona());
        pensionado.setApellidosPersona(request.getApellidosPersona());
        pensionado.setEstadoCivil(request.getEstadoCivil());
        pensionado.setFechaNacimientoPersona(request.getFechaNacimientoPersona());
        pensionado.setFechaExpedicionDocumentoIdPersona(request.getFechaExpedicionDocumentoIdPersona());
        pensionado.setEstadoPersona(request.getEstadoPersona());
        pensionado.setGeneroPersona(request.getGeneroPersona());
        pensionado.setFechaDefuncionPersona(request.getFechaDefuncionPersona());
        pensionado.setFechaInicioPension(request.getFechaInicioPension());
        pensionado.setValorInicialPension(request.getValorInicialPension());
        pensionado.setResolucionPension(request.getResolucionPension());
        pensionado.setEntidadJubilacion(entidadJubilacion);
        pensionado.setAplicarIPCPrimerPeriodo(request.isAplicarIPCPrimerPeriodo());
        
        Pensionado pensionadoGuardado = pensionadoRepositorio.save(pensionado);

        // 2. Crear y guardar la lista de trabajos (Dependencias)
        List<Trabajo> trabajosGuardados = new ArrayList<>();
        if (request.getTrabajos() != null && !request.getTrabajos().isEmpty()) {
            for (RegistroTrabajoPeticion trabajoDto : request.getTrabajos()) {
                Entidad entidadTrabajo = entidadRepositorio.findById(trabajoDto.getNitEntidad())
                        .orElseThrow(() -> new RuntimeException("La entidad con NIT " + trabajoDto.getNitEntidad() + " no está registrada"));
                
                Trabajo trabajo = new Trabajo();
                trabajo.setDiasDeServicio(trabajoDto.getDiasDeServicio());
                trabajo.setEntidad(entidadTrabajo);
                trabajo.setPensionado(pensionadoGuardado);
                trabajosGuardados.add(trabajoRepositorio.save(trabajo));
            }
        }
        
        // 3. Calcular el total de días y actualizar al pensionado
        long totalDias = trabajosGuardados.stream().mapToLong(Trabajo::getDiasDeServicio).sum();
        pensionadoGuardado.setTotalDiasTrabajo(totalDias);
        pensionadoRepositorio.save(pensionadoGuardado);

        // 4. Generar cuotas partes para cada trabajo
        for (Trabajo trabajo : trabajosGuardados) {
            cuotaParteServicio.registrarCuotaParte(trabajo);
        }
    }

    @Transactional
    @Override
    public void actualizarPensionado(Long idPersona, RegistroPensionadoPeticion request) {
        Pensionado pensionadoExistente = pensionadoRepositorio.findById(idPersona)
                .orElseThrow(() -> new RuntimeException("No se encontró el pensionado con ID: " + idPersona));

        Entidad entidadJubilacion = entidadRepositorio.findById(request.getNitEntidad())
                .orElseThrow(() -> new RuntimeException("La Entidad de jubilación no se encuentra registrada"));
        
        pensionadoExistente.setNombrePersona(request.getNombrePersona());
        pensionadoExistente.setApellidosPersona(request.getApellidosPersona());
        pensionadoExistente.setEstadoCivil(request.getEstadoCivil());
        pensionadoExistente.setFechaNacimientoPersona(request.getFechaNacimientoPersona());
        pensionadoExistente.setFechaExpedicionDocumentoIdPersona(request.getFechaExpedicionDocumentoIdPersona());
        pensionadoExistente.setEstadoPersona(request.getEstadoPersona());
        pensionadoExistente.setGeneroPersona(request.getGeneroPersona());
        pensionadoExistente.setFechaDefuncionPersona(request.getFechaDefuncionPersona());
        pensionadoExistente.setFechaInicioPension(request.getFechaInicioPension());
        pensionadoExistente.setValorInicialPension(request.getValorInicialPension());
        pensionadoExistente.setResolucionPension(request.getResolucionPension());
        pensionadoExistente.setEntidadJubilacion(entidadJubilacion);
        pensionadoExistente.setAplicarIPCPrimerPeriodo(request.isAplicarIPCPrimerPeriodo());

        // Lógica de actualización de trabajos: eliminar los viejos y crear los nuevos
        trabajoRepositorio.deleteAll(pensionadoExistente.getTrabajos());
        pensionadoExistente.getTrabajos().clear();

        List<Trabajo> trabajosActualizados = new ArrayList<>();
        if (request.getTrabajos() != null && !request.getTrabajos().isEmpty()) {
            for (RegistroTrabajoPeticion trabajoDto : request.getTrabajos()) {
                Entidad entidadTrabajo = entidadRepositorio.findById(trabajoDto.getNitEntidad())
                        .orElseThrow(() -> new RuntimeException("La entidad con NIT " + trabajoDto.getNitEntidad() + " no está registrada"));
                
                Trabajo trabajo = new Trabajo();
                trabajo.setDiasDeServicio(trabajoDto.getDiasDeServicio());
                trabajo.setEntidad(entidadTrabajo);
                trabajo.setPensionado(pensionadoExistente);
                trabajosActualizados.add(trabajoRepositorio.save(trabajo));
            }
        }
        
        long totalDias = trabajosActualizados.stream().mapToLong(Trabajo::getDiasDeServicio).sum();
        pensionadoExistente.setTotalDiasTrabajo(totalDias);
        
        pensionadoRepositorio.save(pensionadoExistente);
        
        for (Trabajo trabajo : trabajosActualizados) {
            cuotaParteServicio.registrarCuotaParte(trabajo);
        }
    }

    @Override
    public List<PensionadoRespuesta> listarPensionados() {
        List<Pensionado> pensionados = pensionadoRepositorio.findAll();
        return pensionados.stream().map(this::convertirAPensionadoRespuesta).collect(Collectors.toList());
    }
    
    @Override
    public PensionadoRespuesta buscarPensionadoPorId(Long id) {
        Pensionado pensionado = pensionadoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el pensionado con ID: " + id));
        return convertirAPensionadoRespuesta(pensionado);
    }
    
    private PensionadoRespuesta convertirAPensionadoRespuesta(Pensionado pensionado) {
        List<SucesorRespuesta> sucesoresRespuesta = new ArrayList<>();
        if (pensionado.getSucesores() != null) {
            sucesoresRespuesta = pensionado.getSucesores().stream()
                .map(sucesor -> SucesorRespuesta.builder()
                    .idPersona(sucesor.getIdPersona())
                    .numeroIdentificacion(sucesor.getNumeroIdentificacion())
                    .tipoIdentificacion(sucesor.getTipoIdentificacion().name())
                    .nombrePersona(sucesor.getNombrePersona())
                    .apellidosPersona(sucesor.getApellidosPersona())
                    .estadoCivil(sucesor.getEstadoCivil().name())
                    .fechaNacimientoPersona(sucesor.getFechaNacimientoPersona())
                    .fechaExpedicionDocumentoIdPersona(sucesor.getFechaExpedicionDocumentoIdPersona())
                    .estadoPersona(sucesor.getEstadoPersona().name())
                    .generoPersona(sucesor.getGeneroPersona() != null ? sucesor.getGeneroPersona().name() : null)
                    .fechaDefuncionPersona(sucesor.getFechaDefuncionPersona())
                    .fechaInicioSucesion(sucesor.getFechaInicioSucesion())
                    .porcentajePension(sucesor.getPorcentajePension())
                    .build())
                .collect(Collectors.toList());
        }

        List<TrabajoRespuesta> trabajosRespuesta = pensionado.getTrabajos().stream()
                .map(trabajo -> TrabajoRespuesta.builder()
                        .idTrabajo(trabajo.getIdTrabajo())
                        .diasDeServicio(trabajo.getDiasDeServicio())
                        .nitEntidad(trabajo.getEntidad().getNitEntidad())
                        .idPersona(trabajo.getPensionado().getIdPersona())
                        .entidadJubilacion(trabajo.getEntidad().getNombreEntidad())
                        .build())
                .collect(Collectors.toList());

        return PensionadoRespuesta.builder()
                .idPersona(pensionado.getIdPersona())
                .numeroIdentificacion(pensionado.getNumeroIdentificacion())
                .tipoIdentificacion(pensionado.getTipoIdentificacion().name())
                .nombrePersona(pensionado.getNombrePersona())
                .apellidosPersona(pensionado.getApellidosPersona())
                .estadoCivil(pensionado.getEstadoCivil().name())
                .fechaNacimientoPersona(pensionado.getFechaNacimientoPersona())
                .fechaExpedicionDocumentoIdPersona(pensionado.getFechaExpedicionDocumentoIdPersona())
                .estadoPersona(pensionado.getEstadoPersona().name())
                .generoPersona(pensionado.getGeneroPersona() != null ? pensionado.getGeneroPersona().name() : null)
                .fechaInicioPension(pensionado.getFechaInicioPension())
                .valorInicialPension(pensionado.getValorInicialPension())
                .resolucionPension(pensionado.getResolucionPension())
                .nitEntidad(pensionado.getEntidadJubilacion().getNitEntidad())
                .entidadJubilacion(pensionado.getEntidadJubilacion().getNombreEntidad())
                .totalDiasTrabajo(pensionado.getTotalDiasTrabajo())
                .fechaDefuncionPersona(pensionado.getFechaDefuncionPersona())
                .aplicarIPCPrimerPeriodo(pensionado.isAplicarIPCPrimerPeriodo())
                .trabajos(trabajosRespuesta)
                .sucesores(sucesoresRespuesta)
                .build();
    }

    @Transactional
    @Override
    public void desactivarPensionado(Long id) {
        Pensionado pensionado = pensionadoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Pensionado no encontrado con ID: " + id));
        
        // Asumiendo que "RETIRADO" o un estado similar existe en tu enum
        pensionado.setEstadoPersona(EstadoPersona.RETIRADO);
        pensionadoRepositorio.save(pensionado);
    }
    
    // El resto de los métodos de búsqueda deberían funcionar, pero siempre es bueno revisarlos.

    @Override
    public List<Pensionado> buscarPensionadosPorNombre(String nombre) {
        return pensionadoRepositorio.findByNombrePersonaContainingIgnoreCase(nombre);
    }

    @Override
    public List<Pensionado> buscarPensionadosPorApellido(String apellido) {
        return pensionadoRepositorio.findByApellidosPersonaContainingIgnoreCase(apellido);
    }

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

        return pensionadoRepositorio.findByNombrePersonaContainingIgnoreCaseOrApellidosPersonaContainingIgnoreCase(query, query);
    }

    @Override
    public List<EntidadCuotaParteRespuesta> getEntidadesYCuotaParteByPensionadoId(Long pensionadoId) {
        // Este método puede requerir una implementación personalizada en el repositorio
        // si la consulta actual deja de funcionar con los nuevos cambios.
        // Por ahora, lo mantenemos asumiendo que la consulta subyacente sigue siendo válida.
        return pensionadoRepositorio.findEntidadesYCuotaParteByPensionadoId(pensionadoId);
    }
}