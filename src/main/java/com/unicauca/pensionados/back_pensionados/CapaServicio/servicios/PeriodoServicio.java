package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.CuotaParte;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.IPC;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Periodo;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.IPCRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.PeriodoRepositorio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.EditarPeriodoPeticion;
//import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.PeriodoRespuesta;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.PeriodoRespuesta;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PeriodoServicio implements IPeriodoServicio {

    @Autowired
    private PeriodoRepositorio periodoRepositorio;

    @Autowired
    private IPCRepositorio ipcRepositorio;
    @Override
    @Transactional
    public void generarYCalcularPeriodos(LocalDate fechaInicioPension, CuotaParte cuotaParte){
        List<Periodo> periodos = new ArrayList<>();
        LocalDate fechaActual = LocalDate.now();
        int anioInicio = fechaInicioPension.getYear();
        int anioActual = fechaActual.getYear();
    
        // Obtener los IPC desde el año de inicio
        List<IPC> IPCApartirFechaPension = ipcRepositorio.findByFechaIPCGreaterThanEqual(anioInicio);
    
        //Mapa de acceso por año
        Map<Integer, IPC> ipcPorAnio = IPCApartirFechaPension.stream()
            .collect(Collectors.toMap(IPC::getFechaIPC, Function.identity()));
    
        for (int anio = anioInicio; anio <= anioActual; anio++) {
            LocalDate inicioPeriodo;
            LocalDate finPeriodo;
    
            if (anio == anioInicio) {
                inicioPeriodo = fechaInicioPension;
                finPeriodo = LocalDate.of(anio, 12, 31);
            } else if (anio == anioActual) {
                inicioPeriodo = LocalDate.of(anio, 1, 1);
                YearMonth mesAnterior = YearMonth.from(fechaActual).minusMonths(1);
                finPeriodo = mesAnterior.atEndOfMonth();
            } else {
                inicioPeriodo = LocalDate.of(anio, 1, 1);
                finPeriodo = LocalDate.of(anio, 12, 31);
            }
    
            long numeroMeses = ChronoUnit.MONTHS.between(
                YearMonth.from(inicioPeriodo),
                YearMonth.from(finPeriodo)
            ) + 1;
    
            Periodo periodo = new Periodo();
            periodo.setFechaInicioPeriodo(inicioPeriodo);
            periodo.setFechaFinPeriodo(finPeriodo);
            periodo.setNumeroMesadas(numeroMeses);
    
            // Asignar el IPC s
            IPC ipc = ipcPorAnio.get(anio);
            periodo.setIPC(ipc);  // puede ser null si no se encuentra
    
            BigDecimal valorPension = BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(10000, 100000));
            BigDecimal cuotaParteMensual = BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(10000, 100000));
            BigDecimal cuotaParteTotalAnio = BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(10000, 100000));
            BigDecimal incrementoLey476 = BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(10000, 100000));

            periodo.setValorPension(valorPension);
            periodo.setCuotaParteMensual(cuotaParteMensual);
            periodo.setCuotaParteTotalAnio(cuotaParteTotalAnio);
            periodo.setIncrementoLey476(incrementoLey476);
            periodo.setCuotaParte(cuotaParte);
    
            periodos.add(periodo);
        }
    
        // Guardar todos los periodos generados
        periodoRepositorio.saveAll(periodos);
    }
    
    public int calcularMesadas(LocalDate fechaInicio, LocalDate fechaFin){
        return 0;
    }
}