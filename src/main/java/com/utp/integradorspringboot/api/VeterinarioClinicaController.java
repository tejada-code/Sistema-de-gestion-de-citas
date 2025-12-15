package com.utp.integradorspringboot.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.utp.integradorspringboot.models.Veterinario_clinica;
import com.utp.integradorspringboot.models.Veterinario;
import com.utp.integradorspringboot.models.Clinica;
import com.utp.integradorspringboot.repositories.VeterinarioClinicaRepository;
import com.utp.integradorspringboot.repositories.VeterinarioRepository;
import com.utp.integradorspringboot.repositories.ClinicaRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class VeterinarioClinicaController {
    @Autowired
    VeterinarioClinicaRepository veterinarioClinicaRepository;
    @Autowired
    VeterinarioRepository veterinarioRepository;
    @Autowired
    ClinicaRepository clinicaRepository;

    @GetMapping("/VeterinarioClinica")
    public ResponseEntity<List<Veterinario_clinica>> getAll() {
        try {
            List<Veterinario_clinica> lista = new ArrayList<>();
            veterinarioClinicaRepository.findAll().forEach(lista::add);
            if (lista.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(lista, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/VeterinarioClinica/{id}")
    public ResponseEntity<Veterinario_clinica> getById(@PathVariable("id") Long id) {
        Optional<Veterinario_clinica> entidad = veterinarioClinicaRepository.findById(id);
        return entidad.map(vc -> new ResponseEntity<>(vc, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/VeterinarioClinica")
    public ResponseEntity<Veterinario_clinica> create(@RequestBody Veterinario_clinica veterinarioClinica) {
        try {
            if (veterinarioClinica.getVeterinario() == null || veterinarioClinica.getVeterinario().getId() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if (veterinarioClinica.getClinica() == null || veterinarioClinica.getClinica().getId() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            Optional<Veterinario> veterinario = veterinarioRepository.findById(veterinarioClinica.getVeterinario().getId());
            Optional<Clinica> clinica = clinicaRepository.findById(veterinarioClinica.getClinica().getId());
            if (veterinario.isEmpty() || clinica.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            veterinarioClinica.setVeterinario(veterinario.get());
            veterinarioClinica.setClinica(clinica.get());
            Veterinario_clinica nuevo = veterinarioClinicaRepository.save(veterinarioClinica);
            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/VeterinarioClinica/{id}")
    public ResponseEntity<Veterinario_clinica> update(@PathVariable("id") Long id, @RequestBody Veterinario_clinica veterinarioClinica) {
        Optional<Veterinario_clinica> entidad = veterinarioClinicaRepository.findById(id);
        if (entidad.isPresent()) {
            Veterinario_clinica _vc = entidad.get();
            if (veterinarioClinica.getVeterinario() != null && veterinarioClinica.getVeterinario().getId() != null) {
                Optional<Veterinario> veterinario = veterinarioRepository.findById(veterinarioClinica.getVeterinario().getId());
                veterinario.ifPresent(_vc::setVeterinario);
            }
            if (veterinarioClinica.getClinica() != null && veterinarioClinica.getClinica().getId() != null) {
                Optional<Clinica> clinica = clinicaRepository.findById(veterinarioClinica.getClinica().getId());
                clinica.ifPresent(_vc::setClinica);
            }
            return new ResponseEntity<>(veterinarioClinicaRepository.save(_vc), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/VeterinarioClinica/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
        try {
            veterinarioClinicaRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} 