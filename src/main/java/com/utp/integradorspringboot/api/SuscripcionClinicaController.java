package com.utp.integradorspringboot.api;

import com.utp.integradorspringboot.models.Clinica;
import com.utp.integradorspringboot.models.Sesion;
import com.utp.integradorspringboot.models.SuscripcionClinicaDTO;
import com.utp.integradorspringboot.models.Usuario;
import com.utp.integradorspringboot.models.Veterinario;
import com.utp.integradorspringboot.models.Veterinario_clinica;
import com.utp.integradorspringboot.repositories.ClinicaRepository;
import com.utp.integradorspringboot.repositories.SesionRepository;
import com.utp.integradorspringboot.repositories.UsuarioRepository;
import com.utp.integradorspringboot.repositories.VeterinarioClinicaRepository;
import com.utp.integradorspringboot.repositories.VeterinarioRepository;
import com.utp.integradorspringboot.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;


@RestController
@RequestMapping("/api")
public class SuscripcionClinicaController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    VeterinarioRepository veterinarioRepository;

    @Autowired
    ClinicaRepository clinicaRepository;

    @Autowired
    VeterinarioClinicaRepository veterinarioClinicaRepository;

    @Autowired
    SesionRepository sesionRepository;

    @Autowired
    AuthService authService;

    @PostMapping("/Suscripcion")
    @Transactional
    public ResponseEntity<String> suscribirClinica(@RequestBody SuscripcionClinicaDTO dto) {

        System.out.println(dto.toString());
        System.out.println("Fecha de nacimiento: " + dto.getUsuario().getFecha_nacimiento());

        
        try {
            Usuario usuario = this.usuarioRepository.save(dto.getUsuario());

            dto.getVeterinario().setUsuario(usuario);
            Veterinario veterinario = this.veterinarioRepository.save(dto.getVeterinario());

            Clinica clinica = this.clinicaRepository.save(dto.getClinica_veterinaria());

            Veterinario_clinica veterinarioClinica = new Veterinario_clinica();
            veterinarioClinica.setVeterinario(veterinario);
            veterinarioClinica.setClinica(clinica);

            //veterinarioClinica.setFechaInicio(LocalDate.now());
            veterinarioClinicaRepository.save(veterinarioClinica);

            Sesion sesion = dto.getSesion();
            
            if (this.authService.encriptarContrasena(sesion) == false) {
                return new ResponseEntity<>("La Contraseña es nula", HttpStatus.BAD_REQUEST);
            }

            sesion.setUsuario(usuario);
            sesionRepository.save(dto.getSesion());

            return new ResponseEntity<>("Suscripcion creada", HttpStatus.CREATED);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>("Error al procesar la suscripción: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
