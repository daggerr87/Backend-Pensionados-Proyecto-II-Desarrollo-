package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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
    private LocalDate fechaNacimientoPersona;
    private LocalDate fechaExpedicionDocumentoIdPersona;
    private String estadoPersona;
    private String generoPersona;
    private LocalDate fechaDefuncionPersona;
    private Date fechaInicioPension;
    private BigDecimal valorInicialPension;
    private String resolucionPension;
    private String entidadJubilacion;
    private Long totalDiasTrabajo;
    private Long diasDeServicio;
    private Long nitEntidad; 
    private List<TrabajoRespuesta> trabajos;
    
}