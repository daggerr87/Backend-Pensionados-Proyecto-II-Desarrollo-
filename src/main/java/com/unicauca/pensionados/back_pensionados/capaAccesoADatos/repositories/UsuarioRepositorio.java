package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Usuario;
import java.util.List;


public interface UsuarioRepositorio extends JpaRepository<Usuario,Integer>{
    Optional<Usuario> findByUsername(String username);
} 
