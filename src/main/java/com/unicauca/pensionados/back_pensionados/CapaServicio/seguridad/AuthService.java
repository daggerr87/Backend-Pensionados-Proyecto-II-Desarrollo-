package com.unicauca.pensionados.back_pensionados.CapaServicio.seguridad;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Rol;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Usuario;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.UsuarioRepositorio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.LoginPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.AuthRespuesta;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UsuarioRepositorio usuarioRepositorio;
    private final JwtService jwtService; 
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthRespuesta login(LoginPeticion request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails usuario= usuarioRepositorio.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.getToken(usuario);
        return AuthRespuesta.builder()
            .token(token)
            .build();
    }

    public AuthRespuesta register(RegistroPeticion request){
        Usuario usuario = Usuario.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .nombre(request.getNombre())
            .apellido(request.getApellido())
            .role(Rol.USER)
            .build();

            usuarioRepositorio.save(usuario);

            return AuthRespuesta.builder()
                .token(jwtService.getToken(usuario))
                .build();
    }


}
