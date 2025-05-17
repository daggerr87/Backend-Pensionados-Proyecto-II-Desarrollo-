package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PensionadoConTrabajoRespuesta {
    private Long numeroIdPersona;
    private String nombrePersona;
    private String apellidosPersona;
    private Long diasDeServicio; // del trabajo asociado a la entidad
}