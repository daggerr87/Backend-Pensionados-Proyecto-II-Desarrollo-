package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Trabajo;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Trabajo.TrabajoId;

public interface TrabajoRepositorio extends JpaRepository<Trabajo, TrabajoId> {

}
