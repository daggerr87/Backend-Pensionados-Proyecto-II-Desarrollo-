package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.money.Monetary;
import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.CuotaParte;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Pensionado;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Trabajo;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.CuotaParteRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.PeriodoRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.TrabajoRepositorio;

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
    public void registrarCuotaParte(Trabajo trabajo) {
        if (trabajo == null) {
            throw new IllegalArgumentException("El trabajo no puede ser null");
        }
        Pensionado pensionado = trabajo.getPensionado();
        if (pensionado == null) {
            throw new IllegalArgumentException("El trabajo no tiene pensionado asociado");
        }
        if (pensionado.getTotalDiasTrabajo() == null || pensionado.getTotalDiasTrabajo() == 0) {
            throw new IllegalArgumentException("El total de días de trabajo del pensionado es 0 o null");
        }
        if (pensionado.getValorInicialPension() == null) {
            throw new IllegalArgumentException("El valor inicial de pensión es null");
        }
        if (pensionado.getFechaInicioPension() == null) {
            throw new IllegalArgumentException("La fecha de inicio de pensión es null");
        }

        BigDecimal diasDeServicio = BigDecimal.valueOf(trabajo.getDiasDeServicio());
        BigDecimal totalDiasTrabajo = BigDecimal.valueOf(pensionado.getTotalDiasTrabajo());
        BigDecimal porcentajeCuotaParte = diasDeServicio.divide(totalDiasTrabajo, 4, RoundingMode.HALF_UP);
        MonetaryAmount valorInicialPension = Money.of(pensionado.getValorInicialPension(), Monetary.getCurrency("COP"));
        MonetaryAmount valorCuotaParteMoney = valorInicialPension.multiply(porcentajeCuotaParte);

        CuotaParte cuotaParte = new CuotaParte();
        cuotaParte.setTrabajo(trabajo);
        cuotaParte.setValorCuotaParte(valorCuotaParteMoney.getNumber().numberValue(BigDecimal.class));
        cuotaParte.setPorcentajeCuotaParte(porcentajeCuotaParte);
        cuotaParte.setFechaGeneracion(java.util.Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        cuotaParte.setNotas(porcentajeCuotaParte.toString());
        cuotaParte.setValorTotalCuotaParte(cuotaParte.getValorCuotaParte());
        cuotaParteRepositorio.save(cuotaParte);

        // Elimina periodos anteriores si existen (opcional)
        periodoRepositorio.deleteByCuotaParte_IdCuotaParte(cuotaParte.getIdCuotaParte());

        // Conversión segura de fecha
        Date fechaInicioPensionDate = pensionado.getFechaInicioPension();
        LocalDate fechaInicioPension;
        if (fechaInicioPensionDate instanceof java.sql.Date) {
            fechaInicioPension = ((java.sql.Date) fechaInicioPensionDate).toLocalDate();
        } else {
            fechaInicioPension = fechaInicioPensionDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        }
        periodoServicio.generarYCalcularPeriodos(fechaInicioPension, cuotaParte);
    }
    /*@Transactional
    @Override
    public void registrarCuotaParte(Trabajo trabajo){

        if(trabajo != null){
            //Generamos automaticamente la cuota parte que va a estar relacionada a un trabajo
            CuotaParte cuotaParte = new CuotaParte();
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
            cuotaParte.setFechaGeneracion(java.util.Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            cuotaParte.setNotas(porcentajeCuotaParte.toString());
            cuotaParte.setValorTotalCuotaParte(cuotaParte.getValorCuotaParte());
            cuotaParteRepositorio.save(cuotaParte);
            periodoRepositorio.deleteByCuotaParte_IdCuotaParte(cuotaParte.getIdCuotaParte());
            Date fechaInicioPensionDate = trabajo.getPensionado().getFechaInicioPension();
            LocalDate fechaInicioPension = fechaInicioPensionDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
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
            periodoServicio.generarYCalcularPeriodos(fechaInicioPension, cuotaParte);
            
        }*/
        
    

    public CuotaParte buscarPorTrabajoId(Long idTrabajo) {
        return cuotaParteRepositorio.findByTrabajoIdTrabajo(idTrabajo)
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
             CuotaParte cuotaParte = cuotaParteRepositorio.findByTrabajoIdTrabajo(trabajo.getIdTrabajo())
            .orElseGet(() -> {

                CuotaParte nueva = new CuotaParte();
                nueva.setTrabajo(trabajo);
                return nueva;
            });
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
            cuotaParte.setFechaGeneracion(java.util.Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            cuotaParte.setNotas(porcentajeCuotaParte.toString());
            cuotaParte.setValorTotalCuotaParte(cuotaParte.getValorCuotaParte());
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
}