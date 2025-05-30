package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.money.Monetary;
import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.CuotaParte;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Pensionado;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Periodo;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Trabajo;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.CuotaParteRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.PeriodoRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.TrabajoRepositorio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.FiltroCuotaPartePeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.CuotaParteDTO;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.PensionadoConCuotaParteDTO;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.ResultadoCobroPorPensionado;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.ResultadoCobroPorPeriodoDTO;

import jakarta.transaction.Transactional;

@Service
public class CuotaParteServicio implements ICuotaParteServicio {


    private final CuotaParteRepositorio cuotaParteRepositorio;

    private final TrabajoRepositorio trabajoRepositorio;

    private final PeriodoServicio periodoServicio;
   
    private final PeriodoRepositorio periodoRepositorio;

    public CuotaParteServicio (CuotaParteRepositorio cuotaParteRepositorio, TrabajoRepositorio trabajoRepositorio, PeriodoServicio periodoServicio, PeriodoRepositorio periodoRepositorio){
        this.cuotaParteRepositorio = cuotaParteRepositorio;
        this.trabajoRepositorio = trabajoRepositorio;
        this.periodoServicio = periodoServicio;
        this.periodoRepositorio = periodoRepositorio;
    }

    @Transactional
    @Override
    public void registrarCuotaParte(Trabajo trabajo){

            if (trabajo != null) {
            // Buscar si ya existe una cuota parte para este trabajo
            CuotaParte cuotaParte = cuotaParteRepositorio.findByTrabajoIdTrabajo(trabajo.getIdTrabajo())
                .orElse(null);

            if (cuotaParte == null) {
                cuotaParte = new CuotaParte();
                cuotaParte.setTrabajo(trabajo);
            }
            BigDecimal diasDeServicio = BigDecimal.valueOf(trabajo.getDiasDeServicio());
            BigDecimal totalDiasTrabajo = BigDecimal.valueOf(trabajo.getPensionado().getTotalDiasTrabajo());
            if (totalDiasTrabajo.compareTo(BigDecimal.ZERO) == 0) {
                throw new ArithmeticException("Total de dias de trabajo no puede ser cero");
            }
            BigDecimal porcentajeCuotaParte = diasDeServicio.divide(totalDiasTrabajo, 4, RoundingMode.HALF_UP);
            MonetaryAmount valorInicialPension = Money.of(trabajo.getPensionado().getValorInicialPension(), Monetary.getCurrency("COP"));
            MonetaryAmount valorCuotaParteMoney = valorInicialPension.multiply(porcentajeCuotaParte);cuotaParte.setTrabajo(trabajo);
            cuotaParte.setValorCuotaParte(valorCuotaParteMoney.getNumber().numberValue(BigDecimal.class));
            cuotaParte.setPorcentajeCuotaParte(porcentajeCuotaParte);
            cuotaParte.setFechaGeneracion(LocalDate.now());
            cuotaParte.setNotas(porcentajeCuotaParte.toString());
            cuotaParteRepositorio.save(cuotaParte);
            periodoRepositorio.deleteByCuotaParte_IdCuotaParte(cuotaParte.getIdCuotaParte());
            Date fechaInicioPensionDate = trabajo.getPensionado().getFechaInicioPension();
            LocalDate fechaInicioPension = ((java.sql.Date) fechaInicioPensionDate).toLocalDate();
            periodoServicio.generarYCalcularPeriodos(fechaInicioPension, cuotaParte);
            /* 
            MonetaryAmount valorInicialPension = Money.of(trabajo.getPensionado().getValorInicialPension(), Monetary.getCurrency("COP"));
            BigDecimal porcentajeCuotaParte = BigDecimal.valueOf(trabajo.getDiasDeServicio()/trabajo.getPensionado().getTotalDiasTrabajo());
            MonetaryAmount valorCuotaParteMoney=Money.of(0, Monetary.getCurrency("COP"));
            valorCuotaParteMoney = valorInicialPension.multiply(porcentajeCuotaParte);
            cuotaParte.setTrabajo(trabajo);
            cuotaParte.setValorCuotaParte(valorCuotaParteMoney.getNumber().numberValue(BigDecimal.class));
            cuotaParte.setPorcentajeCuotaParte(porcentajeCuotaParte);
            cuotaParte.setFechaGeneracion(LocalDate.now());
            cuotaParte.setNotas("Si funiono la parte de registro de cuota Parte");
            cuotaParteRepositorio.save(cuotaParte);
            cuotaParte.setTrabajo(trabajo);
            Date fechaInicioPensionDate = trabajo.getPensionado().getFechaInicioPension();
            LocalDate fechaInicioPension = ((java.sql.Date) fechaInicioPensionDate).toLocalDate();
            periodoServicio.generarYCalcularPeriodos(fechaInicioPension, cuotaParte);*/
            
        }
        
    }

    public CuotaParte buscarPorTrabajoId(Long idTrabajo) {
        return cuotaParteRepositorio.findById(idTrabajo)
            .orElseThrow(() -> new RuntimeException("CuotaParte no encontrada para trabajo id: " + idTrabajo));
    }

    public void eliminarCuotaPartePorIdTrabajo(Trabajo trabajo) {
        CuotaParte cuotaParte = buscarPorTrabajoId(trabajo.getIdTrabajo());
        cuotaParteRepositorio.delete(cuotaParte);
    }
    

    @Transactional
    @Override
    public void actualizarCuotaParte(Trabajo trabajo) {
        if (trabajo != null) {
            CuotaParte cuotaParte = buscarPorTrabajoId(trabajo.getIdTrabajo());
            BigDecimal diasDeServicio = BigDecimal.valueOf(trabajo.getDiasDeServicio());
            BigDecimal totalDiasTrabajo = BigDecimal.valueOf(trabajo.getPensionado().getTotalDiasTrabajo());
    
            if (totalDiasTrabajo.compareTo(BigDecimal.ZERO) == 0) {
                throw new ArithmeticException("Total de dias de trabajo no puede ser cero");
            }
    
            BigDecimal porcentajeCuotaParte = diasDeServicio.divide(totalDiasTrabajo, 4, RoundingMode.HALF_UP);
            MonetaryAmount valorInicialPension = Money.of(trabajo.getPensionado().getValorInicialPension(), Monetary.getCurrency("COP"));
            MonetaryAmount valorCuotaParteMoney = valorInicialPension.multiply(porcentajeCuotaParte);
    
            cuotaParte.setTrabajo(trabajo);
            cuotaParte.setValorCuotaParte(valorCuotaParteMoney.getNumber().numberValue(BigDecimal.class));
            cuotaParte.setPorcentajeCuotaParte(porcentajeCuotaParte);
            cuotaParte.setFechaGeneracion(LocalDate.now());
            cuotaParte.setNotas(porcentajeCuotaParte.toString());
            cuotaParteRepositorio.save(cuotaParte);
            periodoRepositorio.deleteByCuotaParte_IdCuotaParte(cuotaParte.getIdCuotaParte());
            Date fechaInicioPensionDate = trabajo.getPensionado().getFechaInicioPension();
            LocalDate fechaInicioPension = ((java.sql.Date) fechaInicioPensionDate).toLocalDate();
            periodoServicio.generarYCalcularPeriodos(fechaInicioPension, cuotaParte);
        }
    }
    


    @Transactional
    @Override
    public void recalcularCuotasPartesPorPensionado(Pensionado pensionado){
        List<Trabajo> trabajos = trabajoRepositorio.findByPensionado(pensionado);
        for(Trabajo trabajo : trabajos){
            actualizarCuotaParte(trabajo);
        }
    }

    @Transactional
    public ResultadoCobroPorPeriodoDTO filtrarCuotasPartePorRango(FiltroCuotaPartePeticion filtro) {
        if (filtro == null || filtro.getAnio() == null || filtro.getFechaInicio() == null || filtro.getFechaFin() == null) {
            throw new IllegalArgumentException("Todos los parámetros de filtro son obligatorios");
        }

        int anio = filtro.getAnio();
        int mesInicial = filtro.getFechaInicio();
        int mesFinal = filtro.getFechaFin();

        // Validar meses entre 1 y 12
        if (mesInicial < 1 || mesInicial > 12 || mesFinal < 1 || mesFinal > 12 || mesInicial > mesFinal) {
            throw new IllegalArgumentException("Los meses deben estar entre 1 y 12 y mes inicial debe ser menor o igual al mes final.");
        }

        // Definir rango de fechas
        LocalDate fechaInicio = LocalDate.of(anio, mesInicial, 1);
        LocalDate fechaFin = LocalDate.of(anio, mesFinal, 1).withDayOfMonth(
                LocalDate.of(anio, mesFinal, 1).lengthOfMonth()
        );

        // Obtener todas las cuotas parte en el rango
        List<CuotaParte> cuotasParteEnRango = cuotaParteRepositorio.findByFechaGeneracionBetween(fechaInicio, fechaFin);

        // Agrupar por pensionado
        Map<Long, PensionadoConCuotaParteDTO> mapPensionados = new HashMap<>();

        for (CuotaParte cuota : cuotasParteEnRango) {
            Pensionado pensionado = cuota.getTrabajo().getPensionado();
            Long idPensionado = pensionado.getNumeroIdPersona();

            PensionadoConCuotaParteDTO dto = mapPensionados.computeIfAbsent(idPensionado, k -> {
                return new PensionadoConCuotaParteDTO(
                        pensionado.getNumeroIdPersona().toString(),
                        pensionado.getNombrePersona(),
                        pensionado.getApellidosPersona(),
                        new ArrayList<>(),
                        BigDecimal.ZERO
                );
            });

            CuotaParteDTO cpDto = new CuotaParteDTO(
                    cuota.getIdCuotaParte(),
                    cuota.getValorCuotaParte(),
                    cuota.getFechaGeneracion()
            );

            dto.getCuotasParte().add(cpDto);

            // Sumar al total
            dto.setValorTotalCobro(dto.getValorTotalCobro().add(cuota.getValorCuotaParte()));
        }

        List<PensionadoConCuotaParteDTO> listaPensionados = new ArrayList<>(mapPensionados.values());

        // Calcular total general
        BigDecimal totalGeneral = listaPensionados.stream()
                .map(PensionadoConCuotaParteDTO::getValorTotalCobro)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ResultadoCobroPorPeriodoDTO(listaPensionados, totalGeneral);
    }

    @Override
    public ResultadoCobroPorPensionado obtenerCuotasParteDeUniCauca() {
        // Obtener todas las cuotas parte (o puedes paginar si es necesario)
        List<CuotaParte> todasLasCuotasParte = cuotaParteRepositorio.findAll();

        // Agrupar por pensionado de UniCauca
        Map<Long, PensionadoConCuotaParteDTO> mapPensionados = new HashMap<>();

        for (CuotaParte cuota : todasLasCuotasParte) {
            Pensionado pensionado = cuota.getTrabajo().getPensionado();

            // Verificar si el pensionado pertenece a UniCauca
            if (pensionado == null || 
                !"Universidad del Cauca".equalsIgnoreCase(pensionado.getEntidadJubilacion().getNombreEntidad())) {
                continue; // Saltar si no pertenece a UniCauca
            }

            // Calcular el valor total sumando los cuotaParteTotalAnio de todos los periodos
            BigDecimal valorTotal = cuota.getPeriodos().stream()
                .map(Periodo::getCuotaParteTotalAnio)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            // Asignar el valor total calculado a la cuota parte
            cuota.setValorTotalCuotaParte(valorTotal);

            Long idPensionado = pensionado.getNumeroIdPersona();

            PensionadoConCuotaParteDTO dto = mapPensionados.computeIfAbsent(idPensionado, k -> {
                return new PensionadoConCuotaParteDTO(
                        pensionado.getNumeroIdPersona().toString(),
                        pensionado.getNombrePersona(),
                        pensionado.getApellidosPersona(),
                        new ArrayList<>(),
                        BigDecimal.ZERO
                );
            });

            CuotaParteDTO cpDto = new CuotaParteDTO(
                    cuota.getIdCuotaParte(),
                    cuota.getValorCuotaParte(),
                    cuota.getFechaGeneracion()
            );

            dto.getCuotasParte().add(cpDto);

            // Sumar al total (usamos el valorTotal en lugar de valorCuotaParte)
            dto.setValorTotalCobro(dto.getValorTotalCobro().add(valorTotal));
        }

        List<PensionadoConCuotaParteDTO> listaPensionados = new ArrayList<>(mapPensionados.values());

        // Calcular total general
        BigDecimal totalGeneral = listaPensionados.stream()
                .map(PensionadoConCuotaParteDTO::getValorTotalCobro)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ResultadoCobroPorPensionado(listaPensionados, totalGeneral);
    }
}