package com.unicauca.pensionados.back_pensionados.capaPresentacion.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Entidad;
import com.unicauca.pensionados.back_pensionados.CapaServicio.servicios.ServicioEntidad;
import java.util.List;

@RestController
@RequestMapping("/entidad")
public class ControladorEntidad {
    @Autowired
    private ServicioEntidad entidadService;

    @PostMapping("/registrar")
    public Entidad registrarEntidad(@RequestBody Entidad entidad) {
        return entidadService.registrarEntidad(entidad);
    }

    @GetMapping("/buscarPorNit/{nit}")
    public Entidad buscarPorNit(@PathVariable Long nit) {
        return entidadService.buscarPorNit(nit);
    }

    @GetMapping("/buscarPorCriterio")
    public List<Entidad> buscarPorCriterio(@RequestParam(required = false) String query) {
        if (query == null || query.trim().isEmpty()) {
            return entidadService.listarTodos();
        }
        query = query.replace("\"", "").trim(); // elimina comillas
        return entidadService.buscarEntidadesPorCriterio(query);
    }

    @GetMapping("/listar")
    public List<Entidad> listarTodos() {
        return entidadService.listarTodos();
    }

    @PutMapping("/actualizar/{nid}")
    public Entidad actualizarEntidad(@PathVariable Long id, @RequestBody Entidad entidad) {
        return entidadService.actualizar(id, entidad);
    }
}
