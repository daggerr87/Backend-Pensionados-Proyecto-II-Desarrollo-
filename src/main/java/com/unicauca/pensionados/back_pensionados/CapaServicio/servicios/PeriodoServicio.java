package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.IPC;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Periodo;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.IPCRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.PeriodoRepositorio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.EditarPeriodoPeticion;
//import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.PeriodoRespuesta;
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


   /**
     * Lista todos los periodos disponibles.
     * 
     * @return una lista de objetos Periodo.
     */
    @Override
    public List<PeriodoRespuesta> listarPeriodos() {
        /*List<Periodo> periodos = periodoRepositorio.findAll();
        return periodos.stream()
                .map(periodo -> new PeriodoRespuesta(
                        periodo.getIPC().getFechaIPC(),
                        periodo.getFechaInicioPeriodo(),
                        periodo.getFechaFinPeriodo(),
                        periodo.getIPC().getValorIPC().doubleValue(),
                        periodo.getCuotaParteTotalPeriodo()))
                .collect(Collectors.toList());*/
        return null;
    }
    

    @Override
    public PeriodoRespuesta consultarPeriodoPorAnio(int anio) {
        /*// Buscar el periodo cuyo año de IPC coincida con el año solicitado
        Optional<Periodo> periodoOpt = periodoRepositorio.findAll().stream()
            .filter(p -> p.getIPC() != null && p.getIPC().getFechaIPC() != null && p.getIPC().getFechaIPC() == anio)
            .findFirst();

        Periodo periodo = periodoOpt.orElseThrow(() -> new RuntimeException("No existe periodo para el año: " + anio));

        return PeriodoRespuesta.builder()
            .anio(periodo.getIPC().getFechaIPC())
            .fechaInicioPeriodo(periodo.getFechaInicioPeriodo())
            .fechaFinPeriodo(periodo.getFechaFinPeriodo())
            .ipc(periodo.getIPC().getValorIPC() != null ? periodo.getIPC().getValorIPC().doubleValue() : null)
            .build();*/
            return null; //Hay que corregir 
    }


    @Override
    @Transactional
    public void editarPeriodo(EditarPeriodoPeticion peticion) {
        /*Periodo periodo = periodoRepositorio.findById(peticion.getIdPeriodo())
            .orElseThrow(() -> new RuntimeException("No existe periodo con id: " + peticion.getIdPeriodo()));

        // Actualizar el IPC si es necesario
        if (peticion.getFechaIPC() != null) {
            IPC ipc = ipcRepositorio.findById(Long.valueOf(peticion.getFechaIPC()))
                .orElseThrow(() -> new RuntimeException("No existe IPC para el año: " + peticion.getFechaIPC()));
            periodo.setIPC(ipc);
        }
        
        if (peticion.getFechaInicioPeriodo() != null) {
            periodo.setFechaInicioPeriodo(peticion.getFechaInicioPeriodo());
        }
        if (peticion.getFechaFinPeriodo() != null) {
            periodo.setFechaFinPeriodo(peticion.getFechaFinPeriodo());
        }
        if (peticion.getCuotaParteTotalPeriodo() != null) {
            periodo.setCuotaParteTotalPeriodo(peticion.getCuotaParteTotalPeriodo());
        }
        periodoRepositorio.save(periodo);*/

        //Hay que corregir
    }
}