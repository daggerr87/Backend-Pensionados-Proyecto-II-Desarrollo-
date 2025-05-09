package com.unicauca.pensionados.back_pensionados.capaPresentacion.controladores;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.unicauca.pensionados.back_pensionados.CapaServicio.servicios.ISucesorServicio;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroSucesorPeticion;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/sucesor")
public class SucesorControlador {
    @Autowired
    private ISucesorServicio servicioSucesor;

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

    @GetMapping("/listar")
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

    @GetMapping("/buscarPorId/{id}")
    public ResponseEntity<?> buscarSucesorPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(servicioSucesor.obtenerSucesorPorId(id));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al buscar sucesor: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor: " + ex.getMessage());
        }
    }
    
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarSucesor(@PathVariable Long id, @RequestBody RegistroSucesorPeticion sucesor) {
        try {
            servicioSucesor.editarSucesor(id, sucesor);
            return ResponseEntity.ok("Sucesor actualizado exitosamente");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Error al actualizar sucesor: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Error interno del servidor: " + ex.getMessage());
        }
    }
    
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarSucesor(@PathVariable Long id) {
        try {
            servicioSucesor.eliminarSucesor(id);
            return ResponseEntity.ok("Sucesor eliminado exitosamente");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Error al eliminar sucesor: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Error interno del servidor: " + ex.getMessage());
        }
    }
}
