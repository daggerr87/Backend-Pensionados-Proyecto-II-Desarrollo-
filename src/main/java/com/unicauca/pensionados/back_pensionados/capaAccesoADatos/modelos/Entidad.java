package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ENTIDAD")
@Getter @Setter
public class Entidad {
    @Id
    @Column (name = "nitEntidad")
    private Long nitEntidad;

    @Column (name = "nombreEntidad", nullable = false, length = 100)
    private String nombreEntidad;

    @Column (name ="direccionEntidad", nullable = false, length =100)
    private String direccionEntidad;

    @Column (name = "emailEntidad", nullable =  false, length = 100)
    private String emailEntidad;

    @Column (name = "telefonoEntidad", nullable = false)
    private Long telefonoEntidad;    

    @Column (name = "estadoEntidad", nullable = false, length = 50)
    private String estadoEntidad;

    //relacion 1 a muchos Pensonados
    @OneToMany(mappedBy = "entidadJubilacion")
    private List<Pensionado> pensionados;

    //relacion 1 a muchos pensionados que trabajaron en la entidad
    @OneToMany(mappedBy = "entidad")
    private List<Trabajo> trabajos;

}
