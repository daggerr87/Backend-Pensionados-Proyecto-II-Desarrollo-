package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Deuda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeudaRepositorio extends JpaRepository<Deuda, Long> {
}
