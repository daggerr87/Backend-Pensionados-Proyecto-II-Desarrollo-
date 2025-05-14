package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Entidad;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PensionadoRespuesta {
    
    private Long numeroIdPersona;
    private String tipoIdPersona;
    private String nombrePersona;
    private String apellidosPersona;
    private Date fechaNacimientoPersona;
    private Date fechaExpedicionDocumentoIdPersona;
    private String estadoPersona;
    private String generoPersona;
    private Date fechaDefucncionPersona;
    private Date fechaInicioPension;
    private BigDecimal valorInicialPension;
    private String resolucionPension;
    private Long entidadJubilacion;
    //private Long diasDeServicio;
    private List<TrabajoRespuesta> trabajos;
}
