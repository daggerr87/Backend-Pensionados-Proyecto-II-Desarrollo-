package com.unicauca.pensionados.back_pensionados.capaPresentacion.controladores;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.unicauca.pensionados.back_pensionados.CapaServicio.servicios.CuotaParteServicio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.FiltroCuotaParteEntidadPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.FiltroCuotaPartePeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.EntidadCuotaParteRespuesta;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.EntidadValorCuotaParteDTO;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.ResultadoCobroPorEntidadDTO;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.ResultadoCobroPorPensionado;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.ResultadoCobroPorPeriodoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/cuotaParte")
public class CuotaParteControlador {
    private final CuotaParteServicio cuotaParteServicio;
    private static final Logger logger = LoggerFactory.getLogger(CuotaParteControlador.class);

    public CuotaParteControlador(CuotaParteServicio cuotaParteServicio) {
        this.cuotaParteServicio = cuotaParteServicio;
    }

    @PostMapping("/liquidacion-por-cobrar/filtrar-por-periodo")
    public ResultadoCobroPorPeriodoDTO filtrar(@RequestBody FiltroCuotaPartePeticion filtro) {
        return cuotaParteServicio.obtenerCobroPorPeriodo(filtro);
    }

    @GetMapping("/liquidacion-por-cobrar/cobro-pensionado")
    public ResultadoCobroPorPensionado obtenerCuotasParteUnicauca() {
        return cuotaParteServicio.cuotasPartesPorCobrarPensionado();
    }


    @PostMapping("liquidacion-por-cobrar/cobro-pensionado/{idPensionado}/entidades-por-pensionado-y-periodo")
    public ResponseEntity<?> obtenerEntidadesPorPensionadoYPeriodo(@RequestBody FiltroCuotaPartePeticion filtro,@PathVariable Long idPensionado) {
        try {
            List<EntidadValorCuotaParteDTO> resultado = cuotaParteServicio
                    .listarEntidadesYValorPorPensionadoYRango(idPensionado, filtro);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor");
        }
    }

        



}