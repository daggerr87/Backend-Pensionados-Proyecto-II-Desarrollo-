package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.IPC;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPCRepositorio extends JpaRepository<IPC, java.sql.Date> {
    // ...puedes agregar métodos personalizados si es necesario...
}
