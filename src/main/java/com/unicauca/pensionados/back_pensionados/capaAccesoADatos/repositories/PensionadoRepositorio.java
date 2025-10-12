package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Pensionado;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.enumeradores.TipoIdentificacion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.EntidadCuotaParteRespuesta;

public interface PensionadoRepositorio extends JpaRepository<Pensionado, Long>{
    List<Pensionado> findByNombrePersonaContainingIgnoreCase(String query);
    List<Pensionado> findByApellidosPersonaContainingIgnoreCase(String query);
    List<Pensionado> findByNombrePersonaContainingIgnoreCaseOrApellidosPersonaContainingIgnoreCase(String nombre, String apellido);

    // Este método es necesario para que la clase EntidadServicio pueda encontrar
    // a los pensionados usando su número de documento y su tipo
    Optional<Pensionado> findByTipoIdentificacionAndNumeroIdentificacion(TipoIdentificacion tipo, Long numero);

    @Query("SELECT new com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.EntidadCuotaParteRespuesta(" +
    "t.entidad.nitEntidad, t.entidad.nombreEntidad, SUM(p.cuotaParteTotalAnio)) " +
    "FROM Trabajo t " +
    "JOIN t.pensionado pn " +
    "JOIN t.entidad e " +
    "JOIN CuotaParte cp ON t.idTrabajo = cp.trabajo.idTrabajo " +
    "JOIN Periodo p ON cp.idCuotaParte = p.cuotaParte.idCuotaParte " +
    // Se cambia 'pn.numeroIdPersona' por 'pn.idPersona' para que coincida con el nuevo modelo.
    "WHERE pn.idPersona = :pensionadoId " +
    "AND e.nitEntidad != 8911500319L " +
    "GROUP BY e.nitEntidad, e.nombreEntidad")
    List<EntidadCuotaParteRespuesta> findEntidadesYCuotaParteByPensionadoId(@Param("pensionadoId") Long pensionadoId);
}
