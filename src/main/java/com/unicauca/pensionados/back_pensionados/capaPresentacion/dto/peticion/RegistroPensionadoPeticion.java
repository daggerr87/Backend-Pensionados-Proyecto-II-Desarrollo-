package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

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
    private Date fechaInicioPension;
    private String resolucionPension;
    private BigDecimal valorInicialPension;
    private Long nitEntidad; //Entidad de Jubilacion
    private Long totalDiasTrabajo;
    //private Long diasDeServicio;
    //private Date fechaDefuncionPersona;

    //Datos de Pensionado
    
    

   
     //Lista de trabajos asociados a la entidad 
    private List<RegistroTrabajoPeticion> trabajos;
}
