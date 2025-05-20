package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion;

import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistroSucesorPeticion {
     //Datos de Persona
    private Long numeroIdPersona;
    private String tipoIdPersona;
    private String nombrePersona;
    private String apellidosPersona;
    private Date fechaNacimientoPersona;
    private Date fechaExpedicionDocumentoIdPersona;
    private String estadoPersona;
    private String generoPersona;

    //Datos de Sucesor
    Date fechaInicioSucesion;
    Long pensionado; // Pensionado asociado al sucesor
}
