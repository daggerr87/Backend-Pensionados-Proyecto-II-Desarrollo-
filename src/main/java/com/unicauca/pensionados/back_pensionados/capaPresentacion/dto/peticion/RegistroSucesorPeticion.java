package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

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
    private Date fechaDefuncionPersona;

    //Datos de Sucesor
    Date fechaInicioSucesion;
    Long pensionado; // Pensionado asociado al sucesor
    Double porcentajePension; // Porcentaje de la pensión que le corresponde al sucesor
}
