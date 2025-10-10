package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Deuda;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.DeudaRepositorio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.DeudaDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DeudaServicio implements IDeudaServicio{

    @Autowired
    private DeudaRepositorio deudaRepositorio;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void guardarDeuda(DeudaDTO deuda) {
        try{
            Deuda nuevaDeuda = modelMapper.map(deuda, Deuda.class);
            deudaRepositorio.save(nuevaDeuda);
        }catch (Exception e){
            throw new RuntimeException("No se ha podido guardar la deuda" + e.getMessage());
        }
    }

    @Override
    public void actualizarDeuda(DeudaDTO deuda) {
        try {
            Deuda deudaToUpdate = modelMapper.map(deuda, Deuda.class);
            deudaRepositorio.save(deudaToUpdate);
        } catch (Exception e) {
            throw new RuntimeException("No se ha podido actualizar la deuda" + e.getMessage());
        }
    }

    @Override
    public void eliminarDeuda(Long id) {
        try {
            deudaRepositorio.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("No se ha podido eliminar la deuda" + e.getMessage());
        }
    }

    @Override
    public DeudaDTO obtenerDeudaPorId(Long id) {
        Deuda deuda = deudaRepositorio.findById(id).orElse(null);
        return deuda == null ? null : modelMapper.map(deuda, DeudaDTO.class);
    }

    @Override
    public List<DeudaDTO> listarDeudas() {
        return deudaRepositorio.findAll().stream().map(obj -> modelMapper.map(obj, DeudaDTO.class)).toList();
    }
}
