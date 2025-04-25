package com.unicauca.pensionados.back_pensionados.capaPresentacion.controladores;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unicauca.pensionados.back_pensionados.CapaServicio.servicios.IPensionadoServicio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroPensionadoPeticion;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/pensionado")
public class PensionadoControlador {
    private final IPensionadoServicio pensionadoServicio;

    public PensionadoControlador(IPensionadoServicio pensionadoServicio){
        this.pensionadoServicio = pensionadoServicio;
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarPensionado(@RequestBody RegistroPensionadoPeticion peticion) {
        try{
            pensionadoServicio.registrarPensionado(peticion);
        return ResponseEntity.ok("Pensionado registrado exitosamente");
        } catch (RuntimeException ex) {
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al registrar pensionado: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor: " + ex.getMessage());
        }
        
    }

    @PostMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarPensionado(@RequestBody RegistroPensionadoPeticion peticion) {
        try{
            pensionadoServicio.actualizarPensionado(peticion.getNumeroIdPersona(), peticion);
            return ResponseEntity.ok("Pensionado actualizado exitosamente");
        } catch (RuntimeException ex) {
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al actualzar pensionado: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor: " + ex.getMessage());
        }
    }
    
}
