package com.unicauca.pensionados.back_pensionados.capaPresentacion.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.unicauca.pensionados.back_pensionados.CapaServicio.servicios.IPeriodoServicio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.EditarPeriodoPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.PeriodoRespuesta;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/periodo")
public class PeriodoControlador {

    @Autowired
    private IPeriodoServicio periodoServicio;


    @GetMapping("/listar")
    public ResponseEntity<?> listarPeriodosConIPC() {
        try{
            return ResponseEntity.ok(periodoServicio.listarPeriodos());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al listar periodos: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor: " + ex.getMessage());
        }
    }

    @GetMapping("/buscarPorAnio/{anio}")
    public ResponseEntity<?> consultarPeriodoPorAnio(@PathVariable int anio) {
        try {
            PeriodoRespuesta respuesta = periodoServicio.consultarPeriodoPorAnio(anio);
            return ResponseEntity.ok(respuesta);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Error: " + ex.getMessage());
        }
    }

    @PutMapping("/editar")
    public ResponseEntity<?> editarPeriodo(@RequestBody EditarPeriodoPeticion peticion) {
        try {
            periodoServicio.editarPeriodo(peticion);
            return ResponseEntity.ok("Periodo actualizado exitosamente");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar periodo: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor: " + ex.getMessage());
        }
    }
}