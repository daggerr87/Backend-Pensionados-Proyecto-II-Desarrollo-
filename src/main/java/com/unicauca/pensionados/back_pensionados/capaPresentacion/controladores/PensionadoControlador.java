package com.unicauca.pensionados.back_pensionados.capaPresentacion.controladores;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unicauca.pensionados.back_pensionados.CapaServicio.servicios.IPensionadoServicio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroPensionadoPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.PensionadoRespuesta;

import java.util.List;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Pensionado;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/listar")
    public ResponseEntity<?> listarPensionados() {
        try {
            List<Pensionado> pensionados = pensionadoServicio.listarPensionados();
            return ResponseEntity.ok(pensionados);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al listar pensionados: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor: " + ex.getMessage());
        }
    }
    
   @GetMapping("/buscar/id/{id}")
    public ResponseEntity<?> buscarPensionadosPorId(@PathVariable Long id) {
        try {
            List<Pensionado> pensionados = pensionadoServicio.buscarPensionadoPorId(id);
            if (pensionados == null || pensionados.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró ningún pensionado con el ID: " + id);
            }
            return ResponseEntity.ok(pensionados);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno: " + ex.getMessage());
        }
    }





    @GetMapping("/buscar/nombre")
    public ResponseEntity<?> buscarPensionadosPorNombre(
            @RequestParam(required = false) String nombre) {
        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("El parámetro 'nombre' no puede estar vacío");
            }
            List<Pensionado> pensionados = pensionadoServicio.buscarPensionadosPorNombre(nombre.trim());
            return ResponseEntity.ok(pensionados);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno: " + ex.getMessage());
        }
    }

    @GetMapping("/buscar/apellido")
    public ResponseEntity<?> buscarPensionadosPorApellido(
            @RequestParam(required = false) String apellido) {
        try {
            if (apellido == null || apellido.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("El parámetro 'apellido' no puede estar vacío");
            }
            List<Pensionado> pensionados = pensionadoServicio.buscarPensionadosPorApellido(apellido.trim());
            return ResponseEntity.ok(pensionados);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno: " + ex.getMessage());
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPensionadosPorCriterio(
            @RequestParam(required = false) String query) {
        try {
            List<Pensionado> pensionados = pensionadoServicio.buscarPensionadosPorCriterio(query);
            return ResponseEntity.ok(pensionados);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error en la búsqueda: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno: " + ex.getMessage());
        }
    }

    @PatchMapping("/desactivar/{id}")
    public ResponseEntity<?> desactivarPensionado(@PathVariable Long id) {
        try {
            pensionadoServicio.desactivarPensionado(id);
            return ResponseEntity.ok("Pensionado desactivado exitosamente");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno: " + ex.getMessage());
        }
    }
}
