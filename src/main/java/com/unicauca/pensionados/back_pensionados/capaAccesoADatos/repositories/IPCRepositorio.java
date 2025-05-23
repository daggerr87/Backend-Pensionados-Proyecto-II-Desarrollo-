package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.IPC;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IPCRepositorio extends JpaRepository<IPC, Integer> {
	List<IPC> findByFechaIPCGreaterThanEqual(Integer year);
}