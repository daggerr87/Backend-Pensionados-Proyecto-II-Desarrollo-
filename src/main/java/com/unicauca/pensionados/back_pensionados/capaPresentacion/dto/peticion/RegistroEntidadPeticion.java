package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistroEntidadPeticion {

    @NotNull(message = "El NIT de la entidad no puede ser nulo")
    private Long nitEntidad;

    @NotEmpty(message = "El nombre de la entidad no puede estar vacio")
    private String nombreEntidad;

    @NotEmpty(message = "La direccion de la entidad no puede estar vacia")
    private String direccionEntidad;

    @NotNull(message = "El telefono de la entidad no puede ser nulo")
    private Long telefonoEntidad;

    @NotEmpty(message = "El email de la entidad no puede estar vacio")
    @Email(message = "El email de la entidad no tiene un formato válido")
    private String emailEntidad;

    @NotEmpty(message = "El estado de la entidad no puede estar vacio")
    private String estadoEntidad;

    //Lista de trabajos asociados a la entidad 
    private List<RegistroTrabajoPeticion> trabajos;
}

