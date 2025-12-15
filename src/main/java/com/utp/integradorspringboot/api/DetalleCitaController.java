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

import com.utp.integradorspringboot.models.Detalle_cita;
import com.utp.integradorspringboot.repositories.DetalleCitaRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class DetalleCitaController {

    @Autowired
    DetalleCitaRepository detalleCitaRepository;

    // Obtener todos los detalles de citas
    @GetMapping("/DetalleCita")
    public ResponseEntity<List<Detalle_cita>> getAll() {
        try {
            List<Detalle_cita> lista = new ArrayList<>();
            detalleCitaRepository.findAll().forEach(lista::add);

            if (lista.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(lista, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener detalle de cita por ID
    @GetMapping("/DetalleCita/{id}")
    public ResponseEntity<Detalle_cita> getById(@PathVariable("id") Long id) {
        Optional<Detalle_cita> detalleCitaData = detalleCitaRepository.findById(id);

        if (detalleCitaData.isPresent()) {
            return new ResponseEntity<>(detalleCitaData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Crear nuevo detalle de cita
    @PostMapping("/DetalleCita")
    public ResponseEntity<Detalle_cita> create(@RequestBody Detalle_cita detalleCita) {
        try {
            Detalle_cita nuevoDetalleCita = detalleCitaRepository.save(detalleCita);
            return new ResponseEntity<>(nuevoDetalleCita, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar detalle de cita
    @PutMapping("/DetalleCita/{id}")
    public ResponseEntity<Detalle_cita> update(@PathVariable("id") Long id, @RequestBody Detalle_cita detalleCita) {
        Optional<Detalle_cita> detalleCitaData = detalleCitaRepository.findById(id);

        if (detalleCitaData.isPresent()) {
            Detalle_cita _detalleCita = detalleCitaData.get();
            _detalleCita.setCita(detalleCita.getCita());
            _detalleCita.setEstado(detalleCita.getEstado());
            _detalleCita.setMotivo_cita(detalleCita.getMotivo_cita());
            _detalleCita.setDiagnostico(detalleCita.getDiagnostico());
            _detalleCita.setTratamiento(detalleCita.getTratamiento());
            _detalleCita.setReceta(detalleCita.getReceta());
            _detalleCita.setDuracion_aproximada(detalleCita.getDuracion_aproximada());

            return new ResponseEntity<>(detalleCitaRepository.save(_detalleCita), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar detalle de cita
    @DeleteMapping("/DetalleCita/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
        try {
            detalleCitaRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} 