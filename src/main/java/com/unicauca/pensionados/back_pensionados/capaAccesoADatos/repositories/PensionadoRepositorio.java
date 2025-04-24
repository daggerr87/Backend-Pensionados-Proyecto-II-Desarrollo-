package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Pensionado;

public interface PensionadoRepositorio extends JpaRepository<Pensionado, Long>{

}
