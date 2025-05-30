package com.unicauca.pensionados.back_pensionados.capaPresentacion.controladores;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.unicauca.pensionados.back_pensionados.CapaServicio.servicios.CuotaParteServicio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.FiltroCuotaPartePeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.ResultadoCobroPorPensionado;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.ResultadoCobroPorPeriodoDTO;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/cuotaParte")
public class CuotaParteControlador {
    private final CuotaParteServicio cuotaParteServicio;

    public CuotaParteControlador(CuotaParteServicio cuotaParteServicio) {
        this.cuotaParteServicio = cuotaParteServicio;
    }

    @PostMapping("/filtrar-por-periodo")
    public ResultadoCobroPorPeriodoDTO filtrar(@RequestBody FiltroCuotaPartePeticion filtro) {
        return cuotaParteServicio.filtrarCuotasPartePorRango(filtro);
    }

    @GetMapping("/liquidacion-por-cobrar/cobro-pensionado")
    public ResultadoCobroPorPensionado obtenerCuotasParteUnicauca() {
        return cuotaParteServicio.obtenerCuotasParteDeUniCauca();
    }
    
}