package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion;



import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FiltroCuotaPartePeticion {
    private Integer anio;
    private Integer mesInicial;
    private Integer mesFinal;
}