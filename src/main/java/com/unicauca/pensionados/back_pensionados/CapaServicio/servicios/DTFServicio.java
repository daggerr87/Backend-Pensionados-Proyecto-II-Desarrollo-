package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.DTF;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.DTFRepositorio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.DTFDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DTFServicio implements IDTFServicio {

    @Autowired
    private DTFRepositorio dtfRepositorio;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void guardarDTF(DTFDTO dtf) {
        try{
            DTF nuevoDTF = modelMapper.map(dtf, DTF.class);
            dtfRepositorio.save(nuevoDTF);
        }catch (Exception e){
            throw new RuntimeException("No se ha podido guardar el DTF" + e.getMessage());
        }
    }

    @Override
    public void actualizarDTF(DTFDTO dtf) {
        try{
            DTF dtfToUpdate = modelMapper.map(dtf, DTF.class);
            dtfRepositorio.save(dtfToUpdate);
        }catch (Exception e){
            throw new RuntimeException("No se ha podido actualizar el DTF" + e.getMessage());
        }
    }

    @Override
    public void eliminarDTF(Long id) {
        try {
            dtfRepositorio.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("No se ha podido eliminar el DTF" + e.getMessage());
        }
    }

    @Override
    public DTFDTO obtenerDTFPorId(Long id) {
        DTF dtf = dtfRepositorio.findById(id).orElse(null);
        return dtf == null ? null : modelMapper.map(dtfRepositorio.findById(id), DTFDTO.class);
    }

    @Override
    public DTFDTO obtenerDTFPorMesAnio(Long mes, Long anio) {
        DTF dtf = dtfRepositorio.findByMesAndAnio(mes, anio);
        return dtf == null ? null : modelMapper.map(dtf, DTFDTO.class);
    }

    @Override
    public List<DTFDTO> listarDTFs() {
        return dtfRepositorio.findAll().stream().map(obj -> modelMapper.map(obj, DTFDTO.class)).toList();
    }
}
