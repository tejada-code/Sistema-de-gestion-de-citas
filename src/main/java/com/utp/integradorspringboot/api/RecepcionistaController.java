package com.utp.integradorspringboot.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.utp.integradorspringboot.models.Recepcionista;
import com.utp.integradorspringboot.models.Usuario;
import com.utp.integradorspringboot.models.Clinica;
import com.utp.integradorspringboot.repositories.RecepcionistaRepository;
import com.utp.integradorspringboot.repositories.UsuarioRepository;
import com.utp.integradorspringboot.repositories.ClinicaRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class RecepcionistaController {
    @Autowired
    RecepcionistaRepository recepcionistaRepository;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    ClinicaRepository clinicaRepository;

    @GetMapping("/Recepcionista")
    public ResponseEntity<List<Recepcionista>> getAll() {
        try {
            List<Recepcionista> lista = new ArrayList<>();
            recepcionistaRepository.findAll().forEach(lista::add);
            if (lista.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(lista, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/Recepcionista/{id}")
    public ResponseEntity<Recepcionista> getById(@PathVariable("id") Long id) {
        Optional<Recepcionista> entidad = recepcionistaRepository.findById(id);
        return entidad.map(recepcionista -> new ResponseEntity<>(recepcionista, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/Recepcionista")
    public ResponseEntity<Recepcionista> create(@RequestBody Recepcionista recepcionista) {
        try {
            if (recepcionista.getUsuario() == null || recepcionista.getUsuario().getId() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if (recepcionista.getClinica() == null || recepcionista.getClinica().getId() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            Optional<Usuario> usuario = usuarioRepository.findById(recepcionista.getUsuario().getId());
            Optional<Clinica> clinica = clinicaRepository.findById(recepcionista.getClinica().getId());
            if (usuario.isEmpty() || clinica.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            recepcionista.setUsuario(usuario.get());
            recepcionista.setClinica(clinica.get());
            Recepcionista nuevo = recepcionistaRepository.save(recepcionista);
            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/Recepcionista/{id}")
    public ResponseEntity<Recepcionista> update(@PathVariable("id") Long id, @RequestBody Recepcionista recepcionista) {
        Optional<Recepcionista> entidad = recepcionistaRepository.findById(id);
        if (entidad.isPresent()) {
            Recepcionista _recepcionista = entidad.get();
            if (recepcionista.getUsuario() != null && recepcionista.getUsuario().getId() != null) {
                Optional<Usuario> usuario = usuarioRepository.findById(recepcionista.getUsuario().getId());
                usuario.ifPresent(_recepcionista::setUsuario);
            }
            if (recepcionista.getClinica() != null && recepcionista.getClinica().getId() != null) {
                Optional<Clinica> clinica = clinicaRepository.findById(recepcionista.getClinica().getId());
                clinica.ifPresent(_recepcionista::setClinica);
            }
            return new ResponseEntity<>(recepcionistaRepository.save(_recepcionista), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/Recepcionista/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
        try {
            recepcionistaRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} 