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

import com.utp.integradorspringboot.models.Motivo_cita;
import com.utp.integradorspringboot.repositories.MotivoCitaRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class MotivoCitaController {

    @Autowired
    private MotivoCitaRepository motivoCitaRepository;

    // Obtener todos los motivos de cita
    @GetMapping("/motivos-cita")
    public ResponseEntity<List<Motivo_cita>> getAllMotivos() {
        try {
            List<Motivo_cita> motivos = new ArrayList<>();
            motivoCitaRepository.findAll().forEach(motivos::add);

            if (motivos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(motivos, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener motivo de cita por ID
    @GetMapping("/motivos-cita/{id}")
    public ResponseEntity<Motivo_cita> getMotivoById(@PathVariable("id") Long id) {
        Optional<Motivo_cita> motivoData = motivoCitaRepository.findById(id);

        if (motivoData.isPresent()) {
            return new ResponseEntity<>(motivoData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Crear nuevo motivo de cita
    @PostMapping("/motivos-cita")
    public ResponseEntity<Motivo_cita> createMotivo(@RequestBody Motivo_cita motivo) {
        try {
            Motivo_cita nuevoMotivo = motivoCitaRepository.save(motivo);
            return new ResponseEntity<>(nuevoMotivo, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar motivo de cita
    @PutMapping("/motivos-cita/{id}")
    public ResponseEntity<Motivo_cita> updateMotivo(@PathVariable("id") Long id, @RequestBody Motivo_cita motivo) {
        Optional<Motivo_cita> motivoData = motivoCitaRepository.findById(id);

        if (motivoData.isPresent()) {
            Motivo_cita _motivo = motivoData.get();
            _motivo.setNombre(motivo.getNombre());
            _motivo.setPrecio(motivo.getPrecio());

            return new ResponseEntity<>(motivoCitaRepository.save(_motivo), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar motivo de cita
    @DeleteMapping("/motivos-cita/{id}")
    public ResponseEntity<HttpStatus> deleteMotivo(@PathVariable("id") Long id) {
        try {
            motivoCitaRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} 