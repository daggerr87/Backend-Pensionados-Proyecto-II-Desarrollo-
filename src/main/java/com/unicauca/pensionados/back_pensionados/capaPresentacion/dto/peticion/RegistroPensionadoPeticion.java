package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion;

import java.math.BigDecimal;
//import java.time.LocalDate;
import java.util.List;
import java.util.Date;

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
    private Date fechaDefuncionPersona;

    //Datos de Pensionado
    private Date fechaInicioPension;
    private BigDecimal valorInicialPension;
    private String resolucionPension;
    private Long totalDiasTrabajo;

    private Long nitEntidad; //Entidad de Jubilacion
    private Long diasDeServicio;
    private String entidadJubilacion;

     //Lista de trabajos asociados a la entidad 
     private List<RegistroTrabajoPeticion> trabajos;
}
