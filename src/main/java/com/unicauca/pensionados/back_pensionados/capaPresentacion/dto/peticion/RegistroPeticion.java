package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class RegistroPeticion {

    String username;
    String password;
    String email;
    String nombre;
    String apellido;
    //String rol;  //Agregamos el atributo para manejar el rol "USER"
}
