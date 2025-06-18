package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Petición para registrar un sucesor, incluye datos personales y de sucesión.")
public class RegistroSucesorPeticion {
    @Schema(description = "Número de identificación de la persona sucesora", example = "123456789")
    private Long numeroIdPersona;
    @Schema(description = "Tipo de identificación de la persona", example = "CC")
    private String tipoIdPersona;
    @Schema(description = "Nombres de la persona", example = "Juan")
    private String nombrePersona;
    @Schema(description = "Apellidos de la persona", example = "Pérez")
    private String apellidosPersona;
    @Schema(description = "Fecha de nacimiento de la persona", example = "1980-05-10")
    private Date fechaNacimientoPersona;
    @Schema(description = "Fecha de expedición del documento de identidad", example = "2000-01-01")
    private Date fechaExpedicionDocumentoIdPersona;
    @Schema(description = "Estado de la persona (Activo/Inactivo)", example = "Activo")
    private String estadoPersona;
    @Schema(description = "Género de la persona", example = "M")
    private String generoPersona;
    @Schema(description = "Fecha de defunción de la persona (si aplica)", example = "2024-01-01")
    private Date fechaDefuncionPersona;

    @Schema(description = "Fecha de inicio de la sucesión", example = "2024-06-01")
    Date fechaInicioSucesion;
    @Schema(description = "ID del pensionado asociado al sucesor", example = "987654321")
    Long pensionado;
    @Schema(description = "Porcentaje de la pensión que le corresponde al sucesor", example = "50.0")
    Double porcentajePension;
}
