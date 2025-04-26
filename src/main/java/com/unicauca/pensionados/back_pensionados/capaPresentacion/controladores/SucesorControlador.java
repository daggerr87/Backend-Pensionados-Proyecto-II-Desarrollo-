package com.unicauca.pensionados.back_pensionados.capaPresentacion.controladores;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.unicauca.pensionados.back_pensionados.CapaServicio.servicios.IServicioSucesor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroSucesorPeticion;


@RestController
@RequestMapping("/sucesor")
public class SucesorControlador {
    @Autowired
    private IServicioSucesor servicioSucesor;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarSucesor(@RequestBody RegistroSucesorPeticion peticion) {
        try {
            servicioSucesor.registrarSucesor(peticion);
            return ResponseEntity.ok("Sucesor registrado exitosamente");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al registrar sucesor: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor: " + ex.getMessage());
        }
    }

    @PostMapping("/listar")
    public ResponseEntity<?> listarSucesores() {
        try {
            return ResponseEntity.ok(servicioSucesor.listaSucesores());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al listar sucesores: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor: " + ex.getMessage());
        }
    }

    @PostMapping("/buscarPorId")
    public ResponseEntity<?> buscarSucesorPorId(@RequestBody Long numeroIdPersona) {
        try {
            return ResponseEntity.ok(servicioSucesor.obtenerSucesorPorId(numeroIdPersona));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al buscar sucesor: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor: " + ex.getMessage());
        }
    }
}
