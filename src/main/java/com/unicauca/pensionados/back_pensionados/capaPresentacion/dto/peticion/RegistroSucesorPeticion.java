package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion;

import java.time.LocalDate;
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
    private LocalDate fechaNacimientoPersona;
    private LocalDate fechaExpedicionDocumentoIdPersona;
    private String estadoPersona;
    private String generoPersona;

    //Datos de Sucesor
    private LocalDate fechaInicioSucesion;
    private Long pensionado; // Pensionado asociado al sucesor
}
