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

import com.utp.integradorspringboot.models.Horario_laboral;
import com.utp.integradorspringboot.models.Veterinario;
import com.utp.integradorspringboot.models.Veterinario_horario;
import com.utp.integradorspringboot.repositories.HorarioLaboralRepository;
import com.utp.integradorspringboot.repositories.VeterinarioHorarioRepository;
import com.utp.integradorspringboot.repositories.VeterinarioRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class VeterinarioHorarioController {
    @Autowired
    VeterinarioHorarioRepository veterinarioHorarioRepository;
    @Autowired
    VeterinarioRepository veterinarioRepository;
    @Autowired
    HorarioLaboralRepository horarioLaboralRepository;

    @GetMapping("/VeterinarioHorario")
    public ResponseEntity<List<Veterinario_horario>> getAll() {
        try {
            List<Veterinario_horario> lista = new ArrayList<>();
            veterinarioHorarioRepository.findAll().forEach(lista::add);
            if (lista.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(lista, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/VeterinarioHorario/{id}")
    public ResponseEntity<Veterinario_horario> getById(@PathVariable("id") Long id) {
        Optional<Veterinario_horario> entidad = veterinarioHorarioRepository.findById(id);
        return entidad.map(vh -> new ResponseEntity<>(vh, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/VeterinarioHorario")
    public ResponseEntity<Veterinario_horario> create(@RequestBody Veterinario_horario veterinarioHorario) {
        try {
            if (veterinarioHorario.getVeterinario() == null || veterinarioHorario.getVeterinario().getId() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if (veterinarioHorario.getHorario() == null || veterinarioHorario.getHorario().getId() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            Optional<Veterinario> veterinario = veterinarioRepository.findById(veterinarioHorario.getVeterinario().getId());
            Optional<Horario_laboral> horario = horarioLaboralRepository.findById(veterinarioHorario.getHorario().getId());
            if (veterinario.isEmpty() || horario.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            veterinarioHorario.setVeterinario(veterinario.get());
            veterinarioHorario.setHorario(horario.get());
            Veterinario_horario nuevo = veterinarioHorarioRepository.save(veterinarioHorario);
            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/VeterinarioHorario/{id}")
    public ResponseEntity<Veterinario_horario> update(@PathVariable("id") Long id, @RequestBody Veterinario_horario veterinarioHorario) {
        Optional<Veterinario_horario> entidad = veterinarioHorarioRepository.findById(id);
        if (entidad.isPresent()) {
            Veterinario_horario _vh = entidad.get();
            if (veterinarioHorario.getVeterinario() != null && veterinarioHorario.getVeterinario().getId() != null) {
                Optional<Veterinario> veterinario = veterinarioRepository.findById(veterinarioHorario.getVeterinario().getId());
                veterinario.ifPresent(_vh::setVeterinario);
            }
            if (veterinarioHorario.getHorario() != null && veterinarioHorario.getHorario().getId() != null) {
                Optional<Horario_laboral> horario = horarioLaboralRepository.findById(veterinarioHorario.getHorario().getId());
                horario.ifPresent(_vh::setHorario);
            }
            return new ResponseEntity<>(veterinarioHorarioRepository.save(_vh), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/VeterinarioHorario/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
        try {
            veterinarioHorarioRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/Veterinario/{id}/horarios")
    public ResponseEntity<List<Horario_laboral>> getHorariosByVeterinario(@PathVariable("id") Long id) {
        try {
            List<Veterinario_horario> vhs = veterinarioHorarioRepository.findByVeterinarioId(id);
            if (vhs.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<Horario_laboral> horarios = new ArrayList<>();
            for (Veterinario_horario vh : vhs) {
                if (vh.getHorario() != null) {
                    horarios.add(vh.getHorario());
                }
            }
            return new ResponseEntity<>(horarios, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} 