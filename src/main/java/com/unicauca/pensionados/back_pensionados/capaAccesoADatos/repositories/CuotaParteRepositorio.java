package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.CuotaParte;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuotaParteRepositorio extends JpaRepository<CuotaParte, Long> {
}
