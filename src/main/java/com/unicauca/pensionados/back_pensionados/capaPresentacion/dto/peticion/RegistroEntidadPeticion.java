package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion;

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
    private Long nitEntidad;
    private String nombreEntidad;
    private String direccionEntidad;
    private Long telefonoEntidad;
    private String emailEntidad;
    private String estadoEntidad;
    
    // Lista de trabajos asociados a la entidad
    private List<RegistroTrabajoPeticion> trabajos; 
}
