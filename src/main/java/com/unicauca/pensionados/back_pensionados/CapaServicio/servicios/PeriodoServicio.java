package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.CuotaParte;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.IPC;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Periodo;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.CuotaParteRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.IPCRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.PeriodoRepositorio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistrarPeriodoPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.PeriodoRespuesta;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PeriodoServicio implements IPeriodoServicio {

    @Autowired
    private PeriodoRepositorio periodoRepositorio;

    @Autowired
    private IPCRepositorio ipcRepositorio;

    @Autowired
    private CuotaParteRepositorio cuotaParteRepositorio;


   /**
     * Lista todos los periodos disponibles.
     * <p>
     * Devuelve una lista de periodos, cada uno con todos los campos relevantes expuestos en el DTO {@link PeriodoRespuesta}.
     * </p>
     * @return Lista de periodos ({@link PeriodoRespuesta}) registrados en el sistema.
     */
    @Override
    public List<PeriodoRespuesta> listarPeriodos() {
        List<Periodo> periodos = periodoRepositorio.findAll();
        return periodos.stream()
                .map(periodo -> PeriodoRespuesta.builder()
                        .idPeriodo(periodo.getIdPeriodo())
                        .anio(periodo.getIPC() != null ? periodo.getIPC().getFechaIPC() : null)
                        .fechaInicioPeriodo(periodo.getFechaInicioPeriodo())
                        .fechaFinPeriodo(periodo.getFechaFinPeriodo())
                        .numeroMesadas(periodo.getNumeroMesadas())
                        .valorPension(periodo.getValorPension() != null ? periodo.getValorPension().doubleValue() : null)
                        .cuotaParteMensual(periodo.getCuotaParteMensual() != null ? periodo.getCuotaParteMensual().doubleValue() : null)
                        .cuotaParteTotalAnio(periodo.getCuotaParteTotalAnio() != null ? periodo.getCuotaParteTotalAnio().doubleValue() : null)
                        .incrementoLey476(periodo.getIncrementoLey476() != null ? periodo.getIncrementoLey476().doubleValue() : null)
                        .ipc(periodo.getIPC() != null && periodo.getIPC().getValorIPC() != null ? periodo.getIPC().getValorIPC().doubleValue() : null)
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Consulta un periodo por año (según el año del IPC asociado).
     * <p>
     * Busca el periodo cuyo año IPC coincida con el año solicitado. Devuelve todos los datos relevantes del periodo.
     * </p>
     * @param anio Año del IPC asociado al periodo.
     * @return DTO {@link PeriodoRespuesta} con los datos del periodo encontrado.
     * @throws RuntimeException si no existe un periodo para el año solicitado.
     */
    @Override
    public PeriodoRespuesta consultarPeriodoPorAnio(int anio) {
        
        Optional<Periodo> periodoOpt = periodoRepositorio.findAll().stream()
            .filter(p -> p.getIPC() != null && p.getIPC().getFechaIPC() != null && p.getIPC().getFechaIPC() == anio)
            .findFirst();
        Periodo periodo = periodoOpt.orElseThrow(() -> new RuntimeException("No existe periodo para el año: " + anio));
        return PeriodoRespuesta.builder()
            .idPeriodo(periodo.getIdPeriodo())
            .anio(periodo.getIPC().getFechaIPC())
            .fechaInicioPeriodo(periodo.getFechaInicioPeriodo())
            .fechaFinPeriodo(periodo.getFechaFinPeriodo())
            .numeroMesadas(periodo.getNumeroMesadas())
            .valorPension(periodo.getValorPension() != null ? periodo.getValorPension().doubleValue() : null)
            .cuotaParteMensual(periodo.getCuotaParteMensual() != null ? periodo.getCuotaParteMensual().doubleValue() : null)
            .cuotaParteTotalAnio(periodo.getCuotaParteTotalAnio() != null ? periodo.getCuotaParteTotalAnio().doubleValue() : null)
            .incrementoLey476(periodo.getIncrementoLey476() != null ? periodo.getIncrementoLey476().doubleValue() : null)
            .ipc(periodo.getIPC().getValorIPC() != null ? periodo.getIPC().getValorIPC().doubleValue() : null)
            .build();
    }

    /**
     * Edita los datos de un periodo existente.
     * <p>
     * Solo permite editar periodos cuyo año IPC es igual o mayor al año actual. Valida la existencia del periodo y del IPC asociado.
     * </p>
     * @param idPeriodo id del periodo a editar.
     * @param peticion DTO con los datos a editar
     * @throws RuntimeException si el periodo no existe, el IPC no existe, o el periodo no es editable según las reglas de negocio.
     */
    @Transactional
    public void editarPeriodo(Long idPeriodo, RegistrarPeriodoPeticion peticion) {
        Periodo periodo = periodoRepositorio.findById(idPeriodo)
            .orElseThrow(() -> new RuntimeException("No existe periodo con id: " + idPeriodo));
        int anioActual = java.time.Year.now().getValue();
        int anioPeriodo = periodo.getIPC().getFechaIPC();
        if (anioPeriodo < anioActual) {
            throw new RuntimeException("Solo se pueden editar periodos del año actual en adelante.");
        }
        // Actualizar el IPC si es necesario
        if (peticion.getFechaIPC() != null) {
            IPC ipc = ipcRepositorio.findById(peticion.getFechaIPC())
                .orElseThrow(() -> new RuntimeException("No existe IPC para el año: " + peticion.getFechaIPC()));
            periodo.setIPC(ipc);
        }
        // Actualizar la cuota parte si es necesario
        if (peticion.getIdCuotaParte() != null) {
            CuotaParte cuotaParte = cuotaParteRepositorio.findById(peticion.getIdCuotaParte())
                .orElseThrow(() -> new RuntimeException("No existe cuota parte con id: " + peticion.getIdCuotaParte()));
            periodo.setCuotaParte(cuotaParte);
        }
        if (peticion.getFechaInicioPeriodo() != null) {
            periodo.setFechaInicioPeriodo(peticion.getFechaInicioPeriodo());
        }
        if (peticion.getFechaFinPeriodo() != null) {
            periodo.setFechaFinPeriodo(peticion.getFechaFinPeriodo());
        }
        if (peticion.getNumeroMesadas() != null) {
            periodo.setNumeroMesadas(peticion.getNumeroMesadas());
        }
        if (peticion.getValorPension() != null) {
            periodo.setValorPension(java.math.BigDecimal.valueOf(peticion.getValorPension()));
        }
        if (peticion.getCuotaParteMensual() != null) {
            periodo.setCuotaParteMensual(java.math.BigDecimal.valueOf(peticion.getCuotaParteMensual()));
        }
        if (peticion.getCuotaParteTotalPeriodo() != null) {
            periodo.setCuotaParteTotalAnio(java.math.BigDecimal.valueOf(peticion.getCuotaParteTotalPeriodo()));
        }
        if (peticion.getIncrementoLey476() != null) {
            periodo.setIncrementoLey476(java.math.BigDecimal.valueOf(peticion.getIncrementoLey476()));
        }
        periodoRepositorio.save(periodo);
    }

    /**
     * Registra un nuevo periodo en la base de datos.
     * <p>
     * Valida la existencia del IPC y la cuota parte antes de registrar el periodo. Guarda todos los campos relevantes.
     * </p>
     * @param peticion DTO {@link RegistrarPeriodoPeticion} con los datos del periodo a registrar.
     * @throws RuntimeException si el IPC o la cuota parte no existen.
     */
    @Override
    @Transactional
    public void registrarPeriodo(RegistrarPeriodoPeticion peticion) {
        IPC ipc = ipcRepositorio.findById(peticion.getFechaIPC())
            .orElseThrow(() -> new RuntimeException("No existe IPC para el año: " + peticion.getFechaIPC()));
        // La cuota parte debe existir y es obligatoria
        CuotaParte cuotaParte = cuotaParteRepositorio.findById(peticion.getIdCuotaParte())
            .orElseThrow(() -> new RuntimeException("No existe cuota parte con id: " + peticion.getIdCuotaParte()));
        Periodo periodo = new Periodo();
        periodo.setIPC(ipc);
        periodo.setCuotaParte(cuotaParte);
        periodo.setFechaInicioPeriodo(peticion.getFechaInicioPeriodo());
        periodo.setFechaFinPeriodo(peticion.getFechaFinPeriodo());
        periodo.setNumeroMesadas(peticion.getNumeroMesadas());
        periodo.setValorPension(peticion.getValorPension() != null ? java.math.BigDecimal.valueOf(peticion.getValorPension()) : null);
        periodo.setCuotaParteMensual(peticion.getCuotaParteMensual() != null ? java.math.BigDecimal.valueOf(peticion.getCuotaParteMensual()) : null);
        periodo.setCuotaParteTotalAnio(peticion.getCuotaParteTotalPeriodo() != null ? java.math.BigDecimal.valueOf(peticion.getCuotaParteTotalPeriodo()) : null);
        periodo.setIncrementoLey476(peticion.getIncrementoLey476() != null ? java.math.BigDecimal.valueOf(peticion.getIncrementoLey476()) : null);
        periodoRepositorio.save(periodo);
    }

    /**
     * Elimina un periodo por su id.
     * <p>
     * Elimina el periodo identificado por su id.
     * </p>
     * @param idPeriodo id del periodo a eliminar
     * @throws RuntimeException si el periodo no existe
     */
    @Override
    @Transactional
    public void eliminarPeriodo(Long idPeriodo) {
        Periodo periodo = periodoRepositorio.findById(idPeriodo)
            .orElseThrow(() -> new RuntimeException("No existe periodo con id: " + idPeriodo));
        periodoRepositorio.delete(periodo);
    }
}