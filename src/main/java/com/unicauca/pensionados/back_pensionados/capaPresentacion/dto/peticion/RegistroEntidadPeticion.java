package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion;

import java.util.List;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Trabajo.TrabajoId;

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
    
    private List<Long> pensionados; // Lista de pensionados asociados a la entidad
    private List<TrabajoId> trabajos; // Lista de trabajos asociados a la entidad
}
