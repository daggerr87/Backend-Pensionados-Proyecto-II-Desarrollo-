package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.CuotaParte;


public interface CuotaParteRepositorio extends JpaRepository <CuotaParte, Long> {
    Optional<CuotaParte> findByTrabajoIdTrabajo(Long idTrabajo);
}