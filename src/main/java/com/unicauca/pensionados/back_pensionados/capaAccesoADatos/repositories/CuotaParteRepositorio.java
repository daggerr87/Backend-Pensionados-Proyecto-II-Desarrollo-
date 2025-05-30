package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.CuotaParte;


public interface CuotaParteRepositorio extends JpaRepository <CuotaParte, Long> {
    Optional<CuotaParte> findByTrabajoIdTrabajo(Long idTrabajo);
    List<CuotaParte> findByFechaGeneracionBetween(LocalDate fechaInicio, LocalDate fechaFin);
}