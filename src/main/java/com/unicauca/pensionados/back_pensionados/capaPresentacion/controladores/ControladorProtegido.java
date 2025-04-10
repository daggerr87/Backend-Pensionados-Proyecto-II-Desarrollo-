package com.unicauca.pensionados.back_pensionados.capaPresentacion.controladores;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ControladorProtegido {
    
    @PostMapping("/protegido")
    public String Bienvenido() {
        
        
        return "Bienvenido";
    }
    
}
