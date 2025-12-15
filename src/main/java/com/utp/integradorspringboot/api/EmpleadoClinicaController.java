package com.utp.integradorspringboot.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utp.integradorspringboot.models.Clinica;
import com.utp.integradorspringboot.models.Empleado_clinica;
import com.utp.integradorspringboot.models.Usuario;
import com.utp.integradorspringboot.repositories.ClinicaRepository;
import com.utp.integradorspringboot.repositories.EmpleadoClinicaRepository;
import com.utp.integradorspringboot.repositories.UsuarioRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class EmpleadoClinicaController {

    @Autowired
    EmpleadoClinicaRepository empleadoClinicaRepository;

    @Autowired
    ClinicaRepository clinicaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    // Obtener todos los empleados de clínica
    @GetMapping("/EmpleadoClinica")
    public ResponseEntity<List<Empleado_clinica>> getAll() {
        try {
            List<Empleado_clinica> lista = new ArrayList<>();
            empleadoClinicaRepository.findAll().forEach(lista::add);

            if (lista.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(lista, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener empleado de clínica por ID
    @GetMapping("/EmpleadoClinica/{id}")
    public ResponseEntity<Empleado_clinica> getById(@PathVariable("id") Long id) {
        Optional<Empleado_clinica> empleadoClinicaData = empleadoClinicaRepository.findById(id);

        if (empleadoClinicaData.isPresent()) {
            return new ResponseEntity<>(empleadoClinicaData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Crear nuevo empleado de clínica
    @PostMapping("/EmpleadoClinica")
    public ResponseEntity<Empleado_clinica> create(@RequestBody Empleado_clinica empleadoClinica) {
        try {
            // Validar que el usuario y la clínica existan
            if (empleadoClinica.getUsuario() == null || empleadoClinica.getUsuario().getId() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            if (empleadoClinica.getClinica() == null || empleadoClinica.getClinica().getId() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            Optional<Usuario> usuario = usuarioRepository.findById(empleadoClinica.getUsuario().getId());
            Optional<Clinica> clinica = clinicaRepository.findById(empleadoClinica.getClinica().getId());
            
            if (usuario.isEmpty() || clinica.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            empleadoClinica.setUsuario(usuario.get());
            empleadoClinica.setClinica(clinica.get());
            
            Empleado_clinica nuevoEmpleadoClinica = empleadoClinicaRepository.save(empleadoClinica);
            return new ResponseEntity<>(nuevoEmpleadoClinica, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar empleado de clínica
    @PutMapping("/EmpleadoClinica/{id}")
    public ResponseEntity<Empleado_clinica> update(@PathVariable("id") Long id, @RequestBody Empleado_clinica empleadoClinica) {
        Optional<Empleado_clinica> empleadoClinicaData = empleadoClinicaRepository.findById(id);

        if (empleadoClinicaData.isPresent()) {
            Empleado_clinica _empleadoClinica = empleadoClinicaData.get();
            
            // Actualizar usuario si se proporciona
            if (empleadoClinica.getUsuario() != null && empleadoClinica.getUsuario().getId() != null) {
                Optional<Usuario> usuario = usuarioRepository.findById(empleadoClinica.getUsuario().getId());
                usuario.ifPresent(_empleadoClinica::setUsuario);
            }
            
            // Actualizar clínica si se proporciona
            if (empleadoClinica.getClinica() != null && empleadoClinica.getClinica().getId() != null) {
                Optional<Clinica> clinica = clinicaRepository.findById(empleadoClinica.getClinica().getId());
                clinica.ifPresent(_empleadoClinica::setClinica);
            }
            
            _empleadoClinica.setTipoEmpleado(empleadoClinica.getTipoEmpleado());

            return new ResponseEntity<>(empleadoClinicaRepository.save(_empleadoClinica), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar empleado de clínica
    @DeleteMapping("/EmpleadoClinica/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
        try {
            empleadoClinicaRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} 