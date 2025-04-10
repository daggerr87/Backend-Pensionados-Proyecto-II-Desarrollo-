package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Entidad;
import java.util.List;

public interface EntidadRepositorio extends JpaRepository<Entidad, Long> {
    //Manejo de la tabla Entidad
    List<Entidad> findByNombreEntidadContainingIgnoreCase(String query);
    List<Entidad> findByDireccionEntidadContainingIgnoreCase(String query);
    List<Entidad> findByEmailEntidadContainingIgnoreCase(String query);
    List<Entidad> findByNitEntidadIs(Long query); // si NIT es tipo Long
    //Listar todas las entidades
    List<Entidad> findAllByOrderByNitEntidadAsc(); // ordena por NIT ascendente
}