package com.unicauca.pensionados.back_pensionados.capaPresentacion.controladores;

import com.unicauca.pensionados.back_pensionados.CapaServicio.servicios.IDeudaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/deudas")
public class DeudaControlador {

    @Autowired
    private IDeudaServicio deudaServicio;

    @GetMapping
    public ResponseEntity<?> listarDeudas() {
        try{
            return ResponseEntity.ok(deudaServicio.listarDeudas());
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("estado", "error");
            response.put("mensaje", "Error al listar las deudas: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PutMapping
    public ResponseEntity<?> actualizarDeuda(@org.springframework.web.bind.annotation.RequestBody com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.DeudaDTO deudaDTO) {
        try {
            deudaServicio.actualizarDeuda(deudaDTO);
            return ResponseEntity.ok(deudaDTO);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("estado", "error");
            response.put("mensaje", "Error al actualizar la deuda: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> eliminarDeuda(@RequestParam Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            deudaServicio.eliminarDeuda(id);
            response.put("estado", "exito");
            response.put("mensaje", "Deuda eliminada correctamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("estado", "error");
            response.put("mensaje", "Error al eliminar la deuda: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

}
