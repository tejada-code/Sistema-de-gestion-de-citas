package com.utp.integradorspringboot.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.utp.integradorspringboot.models.Horario_laboral;
import com.utp.integradorspringboot.repositories.HorarioLaboralRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class HorarioLaboralController {
    @Autowired
    HorarioLaboralRepository horarioLaboralRepository;

    @GetMapping("/HorarioLaboral")
    public ResponseEntity<List<Horario_laboral>> getAll() {
        try {
            List<Horario_laboral> lista = new ArrayList<>();
            horarioLaboralRepository.findAll().forEach(lista::add);
            if (lista.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(lista, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/HorarioLaboral/{id}")
    public ResponseEntity<Horario_laboral> getById(@PathVariable("id") Long id) {
        Optional<Horario_laboral> entidad = horarioLaboralRepository.findById(id);
        return entidad.map(horario -> new ResponseEntity<>(horario, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/HorarioLaboral")
    public ResponseEntity<Horario_laboral> create(@RequestBody Horario_laboral horario) {
        try {
            Horario_laboral nuevo = horarioLaboralRepository.save(horario);
            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/HorarioLaboral/{id}")
    public ResponseEntity<Horario_laboral> update(@PathVariable("id") Long id, @RequestBody Horario_laboral horario) {
        Optional<Horario_laboral> entidad = horarioLaboralRepository.findById(id);
        if (entidad.isPresent()) {
            Horario_laboral _horario = entidad.get();
            _horario.setDia_semana(horario.getDia_semana());
            _horario.setHora_inicio(horario.getHora_inicio());
            _horario.setHora_fin(horario.getHora_fin());
            return new ResponseEntity<>(horarioLaboralRepository.save(_horario), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/HorarioLaboral/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
        try {
            horarioLaboralRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} 