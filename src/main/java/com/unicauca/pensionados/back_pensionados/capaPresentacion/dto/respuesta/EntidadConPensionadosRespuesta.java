package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EntidadConPensionadosRespuesta {
    private Long nitEntidad;
    private String nombreEntidad;
    private String direccionEntidad;
    private Long telefonoEntidad;
    private String emailEntidad;
    private String estadoEntidad;
    private List<PensionadoConTrabajoRespuesta> pensionados;
    private List<TrabajoRespuesta> trabajos;
}