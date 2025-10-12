package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Pensionado;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.EntidadCuotaParteRespuesta;

public interface PensionadoRepositorio extends JpaRepository<Pensionado, Long>{
    @Operation(
        summary = "Buscar pensionados por nombre",
        description = "Este endpoint permite buscar pensionados cuyo nombre contenga una cadena de texto específica, sin distinguir entre mayúsculas y minúsculas."
    )
    List<Pensionado> findByNombrePersonaContainingIgnoreCase(String query);
    @Operation(
        summary = "Buscar pensionados por apellidos",
        description = "Este endpoint permite buscar pensionados cuyos apellidos contengan una cadena de texto específica, sin distinguir entre mayúsculas y minúsculas."
    )
    List<Pensionado> findByApellidosPersonaContainingIgnoreCase(String query);
    @Operation(
        summary = "Buscar pensionados por nombre o apellidos",
        description = "Este endpoint permite buscar pensionados cuyo nombre o apellidos contengan una cadena de texto específica, sin distinguir entre mayúsculas y minúsculas."
    )
    List<Pensionado> findByNombrePersonaContainingIgnoreCaseOrApellidosPersonaContainingIgnoreCase(String nombre, String apellido);

    @Operation(
            summary = "Obtener entidades y cuota parte total por pensionado",
            description = "Este endpoint devuelve una lista de entidades junto con la suma total de la cuota parte asociada a un pensionado específico, excluyendo la entidad con NIT 8911500319L."
    )
    @Query("SELECT new com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.EntidadCuotaParteRespuesta(" +
    "t.entidad.nitEntidad, t.entidad.nombreEntidad, SUM(p.cuotaParteTotalAnio)) " +
    "FROM Trabajo t " +
    "JOIN t.pensionado pn " +
    "JOIN t.entidad e " +
    "JOIN CuotaParte cp ON t.idTrabajo = cp.trabajo.idTrabajo " +
    "JOIN Periodo p ON cp.idCuotaParte = p.cuotaParte.idCuotaParte " +
    "WHERE pn.numeroIdPersona = :pensionadoId " +

    "AND e.nitEntidad != unicaucaNit " +
    "GROUP BY e.nitEntidad, e.nombreEntidad")
    List<EntidadCuotaParteRespuesta> findEntidadesYCuotaParteByPensionadoId(@Param("pensionadoId") Long pensionadoId, @Param("unicaucaNit") Long unicaucaNit);

}
