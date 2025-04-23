package com.unicauca.pensionados.back_pensionados.capaPresentacion.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Entidad;
import com.unicauca.pensionados.back_pensionados.CapaServicio.servicios.IServicioEntidad;
import java.util.List;

@RestController
@RequestMapping("/entidad")
public class ControladorEntidad {
    @Autowired
    private IServicioEntidad entidadService;

    @PostMapping("/registrar")
    public Entidad registrarEntidad(@RequestBody Entidad entidad) {
        return entidadService.registrarEntidad(entidad);
    }

    @GetMapping("/buscar")
    public List<Entidad> buscarPorCriterio(@RequestParam(required = false) String query) {
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

    @GetMapping("/listar")
    public List<Entidad> listarTodos() {
        return entidadService.listarTodos();
    }

    @PutMapping("/actualizar/{nid}")
    public Entidad actualizarEntidad(@PathVariable("nid") Long id, @RequestBody Entidad entidad) {
        return entidadService.actualizar(id, entidad);
    }

    @PutMapping("/activar/{nid}")
    public ResponseEntity<Entidad> activarEntidad(@PathVariable Long nid) {
        return entidadService.activarEntidad(nid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/desactivar/{nid}")
    public ResponseEntity<Entidad> desactivarEntidad(@PathVariable Long nid) {
        return entidadService.desactivarEntidad(nid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

