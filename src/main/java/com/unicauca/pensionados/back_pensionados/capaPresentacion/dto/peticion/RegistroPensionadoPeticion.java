package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion;

import java.math.BigDecimal;
import java.sql.Date;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class RegistroPensionadoPeticion {
    //Datos de Persona
    private Long numeroIdPersona;
    private String tipoIdPersona;
    private String nombrePersona;
    private String apellidosPersona;
    private Date fechaNacimientoPersona;
    private Date fechaExpedicionDocumentoIdPersona;
    private String estadoPersona;
    private String generoPersona;

    //Datos de Pensionado
    private Date fechaInicioPension;
    private BigDecimal valorPension;
    private String resolucionPension;
    private Long totalDiasTrabajo;

    private Long nitEntidad; //Entidad de Jubilacion
    private Long diasDeServicio;
}
