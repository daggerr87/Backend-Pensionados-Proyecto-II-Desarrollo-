package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.CuotaAnual;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.CuotaAnualRepositorio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.CuotaAnualDTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CuotaAnualServicio implements ICuotaAnualServicio{
    @Autowired
    private CuotaAnualRepositorio cuotaAnualRepositorio;
    @Autowired
    private org.modelmapper.ModelMapper modelMapper;

    @Override
    public void guardarCuotaAnual(CuotaAnualDTO cuotaAnual) {
        try{
            CuotaAnual cuotaAnualToSave = modelMapper.map(cuotaAnual, CuotaAnual.class);

            cuotaAnualRepositorio.save(cuotaAnualToSave);
        }catch (Exception e){
            throw new RuntimeException("No se ha podido guardar la cuota anual" + e.getMessage());
        }
    }

    @Override
    public void actualizarCuotaAnual(CuotaAnualDTO cuotaAnual) {
        try{
            CuotaAnual cuotaAnualToSave = modelMapper.map(cuotaAnual, CuotaAnual.class);
            cuotaAnualRepositorio.save(cuotaAnualToSave);
        }catch (Exception e){
            throw new RuntimeException("No se ha podido actualizar la cuota anual" + e.getMessage());
        }
    }

    @Override
    public void eliminarCuotaAnual(Long id) {
        try{
            cuotaAnualRepositorio.deleteById(id);
        }catch (Exception e){
            throw new RuntimeException("No se ha podido eliminar la cuota anual" + e.getMessage());
        }
    }

    @Override
    public CuotaAnualDTO obtenerCuotaAnualPorAnio(Long anio) {

        return modelMapper.map(cuotaAnualRepositorio.findByAnio(anio), CuotaAnualDTO.class);
    }

    @Override
    public CuotaAnualDTO obtenerCuotaAnualPorId(Long id) {
        return modelMapper.map(cuotaAnualRepositorio.findById(id), CuotaAnualDTO.class);
    }

    @Override
    public List<CuotaAnualDTO> listarCuotasAnuales() {
        return cuotaAnualRepositorio.findAll().stream().map(obj -> modelMapper.map(obj, CuotaAnualDTO.class)).toList();
    }
}
