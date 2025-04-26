package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Pensionado;

public interface PensionadoRepositorio extends JpaRepository<Pensionado, Long>{
    List<Pensionado> findByNombrePersonaContainingIgnoreCase(String query);
    List<Pensionado> findByApellidosPersonaContainingIgnoreCase(String query);
    List<Pensionado> findByNombrePersonaContainingIgnoreCaseOrApellidosPersonaContainingIgnoreCase(String nombre, String apellido);
}
