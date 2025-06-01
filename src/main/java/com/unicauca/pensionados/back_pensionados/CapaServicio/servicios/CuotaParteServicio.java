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
import java.util.stream.Collectors;

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



    @Override
    public ResultadoCobroPorPensionado  cuotasPartesPorCobrarPensionado() {
        List<CuotaParte> todasLasCuotasParte = cuotaParteRepositorio.findAll();
    
        Map<Long, PensionadoConCuotaParteDTO> mapPensionados = new HashMap<>();
    
        for (CuotaParte cuota : todasLasCuotasParte) {
            Pensionado pensionado = cuota.getTrabajo().getPensionado();
    
            if (pensionado == null
                || pensionado.getEntidadJubilacion() == null
                || !"Universidad del Cauca".equalsIgnoreCase(pensionado.getEntidadJubilacion().getNombreEntidad())) {
                // Ignorar pensionados que no sean de UniCauca
                continue;
            }
    
            // revisamos la entidad de la CUOTA PARTE para omitir las cuotas de entidad con nit 8911500319L que pertenecen a unicauca
        
            Long nitEntidadCuota = cuota.getTrabajo().getEntidad().getNitEntidad();
            if (nitEntidadCuota != null && nitEntidadCuota.equals(8911500319L)) {
                // Omitir cuota parte que pertenece a unicauca
                continue;
            }
    
            Long idPensionado = pensionado.getNumeroIdPersona();
    
            PensionadoConCuotaParteDTO dto = mapPensionados.computeIfAbsent(idPensionado, k ->
                new PensionadoConCuotaParteDTO(
                    pensionado.getNumeroIdPersona().toString(),
                    pensionado.getNombrePersona(),
                    pensionado.getApellidosPersona(),
                    new ArrayList<>(),
                    BigDecimal.ZERO
                )
            );
    
            CuotaParteDTO cpDto = new CuotaParteDTO(
                cuota.getIdCuotaParte(),
                cuota.getValorCuotaParte(),
                cuota.getFechaGeneracion()
            );
    
            dto.getCuotasParte().add(cpDto);
    
            // Sumar el valor de la cuota parte al total del pensionado
            dto.setValorTotalCobro(dto.getValorTotalCobro().add(cuota.getValorCuotaParte()));
        }
    
        List<PensionadoConCuotaParteDTO> listaPensionados = new ArrayList<>(mapPensionados.values());
    
        BigDecimal totalGeneral = listaPensionados.stream()
            .map(PensionadoConCuotaParteDTO::getValorTotalCobro)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    
        return new ResultadoCobroPorPensionado(listaPensionados, totalGeneral);
    }
    
    public ResultadoCobroPorPeriodoDTO obtenerCobroPorPeriodo(FiltroCuotaPartePeticion filtro) {
        List<CuotaParte> todasLasCuotasParte = cuotaParteRepositorio.findAll();
        Map<Long, PensionadoConCuotaParteDTO> mapPensionados = new HashMap<>();
        int anio = filtro.getAnio();
        int mesInicio = 1;
        if(filtro.getMesInicial() != null){
            mesInicio = filtro.getMesInicial();
        }
        int mesFinal = 12;
        if(filtro.getMesFinal() != null){
            mesFinal = filtro.getMesFinal();
        }
        int mesada = calcularMesadas(mesInicio, mesFinal, anio);
        BigDecimal mesadaDecimal = BigDecimal.valueOf(mesada);
    
        for (CuotaParte cuota : todasLasCuotasParte) {
            Pensionado pensionado = cuota.getTrabajo().getPensionado();
    
            if (pensionado == null
                || pensionado.getEntidadJubilacion() == null
                || !"Universidad del Cauca".equalsIgnoreCase(pensionado.getEntidadJubilacion().getNombreEntidad())) {
                continue;
            }
    
            Long nitEntidadCuota = cuota.getTrabajo().getEntidad().getNitEntidad();
            if (nitEntidadCuota != null && nitEntidadCuota.equals(8911500319L)) {
                continue;
            }
    
            List<Periodo> periodos = cuota.getPeriodos();
            if (periodos == null || periodos.isEmpty()) {
                continue;
            }
    
            // Filtrar periodos por año y multiplicar cuotaParteMensual * numero de mesadas
            BigDecimal totalPorAnio = periodos.stream()
                .filter(p -> p.getFechaInicioPeriodo() != null && p.getFechaInicioPeriodo().getYear() == anio)
                .map(Periodo::getCuotaParteMensual)
                .filter(Objects::nonNull)
                .map(cuotaMensual -> cuotaMensual.multiply(mesadaDecimal))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    
            if (totalPorAnio.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }
    
            Long idPensionado = pensionado.getNumeroIdPersona();
            PensionadoConCuotaParteDTO dto = mapPensionados.computeIfAbsent(idPensionado, k ->
                new PensionadoConCuotaParteDTO(
                    pensionado.getNumeroIdPersona().toString(),
                    pensionado.getNombrePersona(),
                    pensionado.getApellidosPersona(),
                    new ArrayList<>(),
                    BigDecimal.ZERO
                )
            );
    
            CuotaParteDTO cpDto = new CuotaParteDTO(
                cuota.getIdCuotaParte(),
                totalPorAnio,
                cuota.getFechaGeneracion()
            );
    
            dto.getCuotasParte().add(cpDto);
            dto.setValorTotalCobro(dto.getValorTotalCobro().add(totalPorAnio));
        }
    
        List<PensionadoConCuotaParteDTO> listaPensionados = new ArrayList<>(mapPensionados.values());
    
        BigDecimal totalGeneral = listaPensionados.stream()
            .map(PensionadoConCuotaParteDTO::getValorTotalCobro)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    
        return new ResultadoCobroPorPeriodoDTO(listaPensionados, totalGeneral);
    }
    
    private int calcularMesadas(int mesInicio, int mesFinal, int anioPension) {
        if (mesInicio < 1 || mesInicio > 12) {
            throw new IllegalArgumentException("mesInicio debe estar entre 1 y 12");
        }
        if (mesFinal < 1 || mesFinal > 12) {
            throw new IllegalArgumentException("mesFinal debe estar entre 1 y 12");
        }
        if (mesInicio > mesFinal) {
            throw new IllegalArgumentException("mesInicio no puede ser mayor que mesFinal");
        }
    
        int mesadasOrdinarias = mesFinal - mesInicio + 1;
    
        int adicionales = 0;
        if (anioPension < 1993) {
            if (mesFinal >= 11 && mesInicio <= 11) {
                adicionales = 1;
            }
        } else {
            if (mesFinal >= 6 && mesInicio <= 6) adicionales++;
            if (mesFinal >= 11 && mesInicio <= 11) adicionales++;
        }
    
        return mesadasOrdinarias + adicionales;
    }
    
    
}