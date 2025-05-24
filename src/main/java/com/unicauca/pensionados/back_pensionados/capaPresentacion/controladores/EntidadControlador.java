package com.unicauca.pensionados.back_pensionados.capaPresentacion.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroEntidadPeticion;
import com.unicauca.pensionados.back_pensionados.CapaServicio.servicios.IEntidadServicio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Entidad;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroTrabajoPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.EntidadConPensionadosRespuesta;

import java.util.List;

@RestController
@RequestMapping("/entidad")
public class EntidadControlador {
    @Autowired
    private IEntidadServicio entidadService;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarEntidad(@RequestBody RegistroEntidadPeticion entidad) {
        try{
            entidadService.registrarEntidad(entidad);
            return ResponseEntity.ok("Entidad registrada exitosamente");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Error al registrar entidad: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Error interno del servidor: " + ex.getMessage());
        }
    }

    @GetMapping("/buscar")
    public List<EntidadConPensionadosRespuesta> buscarPorCriterio(@RequestParam(required = false) String query) {
        if (query == null || query.trim().isEmpty()) {
            return entidadService.listarTodos();
        }
        query = query.replace("\"", "").trim(); // elimina comillas
        return entidadService.buscarEntidadesPorCriterio(query);
    }
    
    @GetMapping("/buscarPorNombre")
    public ResponseEntity<List<Entidad>> buscarPorNombre(@RequestParam String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return ResponseEntity.badRequest().build(); // Devuelve un error 400 si el nombre está vacío
        }
        List<Entidad> entidades = entidadService.buscarEntidadPorNombre(nombre.trim());
        return entidades.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(entidades);
    }

    @GetMapping("/buscarPorNit/{nit}")
    public ResponseEntity<Entidad> buscarPorNit(@PathVariable Long nit) {
        try {
            Entidad entidad = entidadService.buscarPorNit(nit);
            return ResponseEntity.ok(entidad);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/listar")
    public List<EntidadConPensionadosRespuesta> listarTodos() {
        try {
            return entidadService.listarTodos();
        } catch (RuntimeException ex) {
            throw new RuntimeException("Error al listar entidades: " + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error interno del servidor: " + ex.getMessage());
        }
    }

    @PutMapping("/actualizar/{nid}")
    public ResponseEntity<?> actualizarEntidad(@PathVariable("nid") Long id, @RequestBody RegistroEntidadPeticion entidad) {
        try {
            entidadService.actualizar(id, entidad);
            return ResponseEntity.ok("Entidad actualizada exitosamente");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Error al actualizar entidad: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Error interno del servidor: " + ex.getMessage());
        }
    }

    @PutMapping("/activar/{nid}")
    public ResponseEntity<?> activarEntidad(@PathVariable Long nid) {
        try {
            boolean resultado = entidadService.activarEntidad(nid);
            if (resultado) {
                return ResponseEntity.ok("Entidad activada exitosamente");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró la entidad con NIT: " + nid);
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al activar la entidad: " + ex.getMessage());
        }
    }

    @PutMapping("/desactivar/{nid}")
    public ResponseEntity<?> desactivarEntidad(@PathVariable Long nid) {
        try {
            boolean resultado = entidadService.desactivarEntidad(nid);
            if (resultado) {
                return ResponseEntity.ok("Entidad desactivada exitosamente");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró la entidad con NIT: " + nid);
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al desactivar la entidad: " + ex.getMessage());
        }
    }

    @PutMapping("/editarPensionados/{nitEntidad}")
    public ResponseEntity<?> editarPensionadosDeEntidad(@PathVariable Long nitEntidad, @RequestBody List<RegistroTrabajoPeticion> trabajosActualizados) {
        try {
            entidadService.editarPensionadosDeEntidad(nitEntidad, trabajosActualizados);
            return ResponseEntity.ok("Pensionados de la entidad actualizados exitosamente");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Error al actualizar pensionados: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor: " + ex.getMessage());
        }
    }
}
