package com.unicauca.pensionados.back_pensionados.capaPresentacion.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroEntidadPeticion;
import com.unicauca.pensionados.back_pensionados.CapaServicio.servicios.IServicioEntidad;

import java.util.List;

@RestController
@RequestMapping("/entidad")
public class ControladorEntidad {
    @Autowired
    private IServicioEntidad entidadService;

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
    public List<RegistroEntidadPeticion> buscarPorCriterio(@RequestParam(required = false) String query) {
        if (query == null || query.trim().isEmpty()) {
            return entidadService.listarTodos();
        }
        query = query.replace("\"", "").trim(); // elimina comillas
        return entidadService.buscarEntidadesPorCriterio(query);
    }
    
    @GetMapping("/buscarPorNombre")
    public ResponseEntity<List<RegistroEntidadPeticion>> buscarPorNombre(@RequestParam String nombre) {
    if (nombre == null || nombre.trim().isEmpty()) {
        return ResponseEntity.badRequest().build(); // Devuelve un error 400 si el nombre está vacío
    }
    List<RegistroEntidadPeticion> entidades = entidadService.buscarEntidadPorNombre(nombre.trim());
    return entidades.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(entidades);
    }

    @GetMapping("/listar")
    public List<RegistroEntidadPeticion> listarTodos() {
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
    public ResponseEntity<RegistroEntidadPeticion> activarEntidad(@PathVariable Long nid) {
        return entidadService.activarEntidad(nid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/desactivar/{nid}")
    public ResponseEntity<RegistroEntidadPeticion> desactivarEntidad(@PathVariable Long nid) {
        return entidadService.desactivarEntidad(nid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

