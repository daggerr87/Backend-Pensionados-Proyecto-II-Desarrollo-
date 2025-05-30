package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import java.math.BigDecimal;
import java.util.List;

public class PensionadoConCuotaParteDTO {
    private String cedula;
    private String nombres;
    private String apellidos;
    private List<CuotaParteDTO> cuotasParte;
    private BigDecimal valorTotalCobro;

    public PensionadoConCuotaParteDTO() {}

    public PensionadoConCuotaParteDTO(String cedula, String nombres, String apellidos, List<CuotaParteDTO> cuotasParte, BigDecimal valorTotalCobro) {
        this.cedula = cedula;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.cuotasParte = cuotasParte;
        this.valorTotalCobro = valorTotalCobro;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public List<CuotaParteDTO> getCuotasParte() {
        return cuotasParte;
    }

    public void setCuotasParte(List<CuotaParteDTO> cuotasParte) {
        this.cuotasParte = cuotasParte;
    }

    public BigDecimal getValorTotalCobro() {
        return valorTotalCobro;
    }

    public void setValorTotalCobro(BigDecimal valorTotalCobro) {
        this.valorTotalCobro = valorTotalCobro;
    }
}