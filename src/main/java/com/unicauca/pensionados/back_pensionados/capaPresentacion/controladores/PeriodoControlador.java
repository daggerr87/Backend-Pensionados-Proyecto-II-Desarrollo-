package com.unicauca.pensionados.back_pensionados.capaPresentacion.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.unicauca.pensionados.back_pensionados.CapaServicio.servicios.IPeriodoServicio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistrarPeriodoPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.PeriodoRespuesta;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Controlador REST para la gestión de periodos.
 * <p>
 * Expone endpoints para listar, consultar, registrar, editar y eliminar periodos.
 * Cada endpoint maneja las reglas de negocio y validaciones definidas en el servicio.
 * </p>
 */
@RestController
@RequestMapping("/periodo")
public class PeriodoControlador {

    @Autowired
    private IPeriodoServicio periodoServicio;


    /**
     * Lista todos los periodos registrados en el sistema.
     * @return Lista de periodos con todos los campos relevantes expuestos en el DTO.
     */
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

    /**
     * Consulta un periodo por año (según el año del IPC asociado).
     * @param anio Año del IPC asociado al periodo.
     * @return Periodo encontrado o mensaje de error si no existe.
     */
    @GetMapping("/buscarPorAnio/{anio}")
    public ResponseEntity<?> consultarPeriodoPorAnio(@PathVariable int anio) {
        try {
            PeriodoRespuesta respuesta = periodoServicio.consultarPeriodoPorAnio(anio);
            return ResponseEntity.ok(respuesta);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Error: " + ex.getMessage());
        }
    }

    /**
     * Edita los datos de un periodo existente.
     * <p>
     * Solo permite editar periodos cuyo año IPC es igual o mayor al actual.
     * El id del periodo a editar se recibe como parámetro en la URL.
     * </p>
     * @param idPeriodo id del periodo a editar.
     * @param peticion DTO con los datos a editar (sin necesidad de incluir el id).
     * @return Mensaje de éxito o error según el resultado de la operación.
     */
    @PutMapping("/editar/{idPeriodo}")
    public ResponseEntity<?> editarPeriodo(@PathVariable Long idPeriodo, @RequestBody RegistrarPeriodoPeticion peticion) {
        try {
            periodoServicio.editarPeriodo(idPeriodo, peticion);
            return ResponseEntity.ok("Periodo actualizado exitosamente");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar periodo: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor: " + ex.getMessage());
        }
    }

    /**
     * Registra un nuevo periodo en la base de datos.
     * <p>
     * Valida la existencia del IPC y la cuota parte antes de registrar el periodo.
     * </p>
     * @param peticion DTO con los datos del periodo a registrar.
     * @return Mensaje de éxito o error según el resultado de la operación.
     */
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarPeriodo(@RequestBody RegistrarPeriodoPeticion peticion) {
        try {
            periodoServicio.registrarPeriodo(peticion);
            return ResponseEntity.status(HttpStatus.CREATED).body("Periodo registrado exitosamente");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al registrar periodo: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor: " + ex.getMessage());
        }
    }

    /**
     * Elimina un periodo por su id.
     * <p>
     * Elimina el periodo identificado por su id.
     * </p>
     * @param id id del periodo a eliminar
     * @return Mensaje de éxito o error según el resultado de la operación.
     */
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarPeriodo(@PathVariable Long id) {
        try {
            periodoServicio.eliminarPeriodo(id);
            return ResponseEntity.ok("Periodo eliminado exitosamente");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al eliminar periodo: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor: " + ex.getMessage());
        }
    }
}