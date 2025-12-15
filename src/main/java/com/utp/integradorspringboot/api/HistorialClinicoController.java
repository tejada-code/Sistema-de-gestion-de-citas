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

import com.utp.integradorspringboot.models.Historial_clinico;
import com.utp.integradorspringboot.models.Cita;
import com.utp.integradorspringboot.models.Mascota;
import com.utp.integradorspringboot.repositories.HistorialClinicoRepository;
import com.utp.integradorspringboot.repositories.GestionCitaRepository;
import com.utp.integradorspringboot.repositories.MascotaRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class HistorialClinicoController {

    @Autowired
    HistorialClinicoRepository historialClinicoRepository;

    @Autowired
    GestionCitaRepository citaRepository;

    @Autowired
    MascotaRepository mascotaRepository;

    // Obtener todos los historiales clínicos
    @GetMapping("/HistorialClinico")
    public ResponseEntity<List<Historial_clinico>> getAll() {
        try {
            List<Historial_clinico> lista = new ArrayList<>();
            historialClinicoRepository.findAll().forEach(lista::add);

            if (lista.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(lista, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener historial clínico por ID
    @GetMapping("/HistorialClinico/{id}")
    public ResponseEntity<Historial_clinico> getById(@PathVariable("id") Long id) {
        Optional<Historial_clinico> historialClinicoData = historialClinicoRepository.findById(id);

        if (historialClinicoData.isPresent()) {
            return new ResponseEntity<>(historialClinicoData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Obtener historiales clínicos por ID de mascota
    @GetMapping("/HistorialClinico/mascota/{mascotaId}")
    public ResponseEntity<List<Historial_clinico>> getByMascotaId(@PathVariable("mascotaId") Long mascotaId) {
        try {
            List<Historial_clinico> historiales = new ArrayList<>();
            historialClinicoRepository.findAll().forEach(historial -> {
                if (historial.getMascota() != null && historial.getMascota().getId().equals(mascotaId)) {
                    historiales.add(historial);
                }
            });

            if (historiales.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(historiales, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Crear nuevo historial clínico
    @PostMapping("/HistorialClinico")
    public ResponseEntity<Historial_clinico> create(@RequestBody Historial_clinico historialClinico) {
        try {
            // Validar que la cita y la mascota existan
            if (historialClinico.getCita() == null || historialClinico.getCita().getId() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            if (historialClinico.getMascota() == null || historialClinico.getMascota().getId() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            Optional<Cita> cita = citaRepository.findById(historialClinico.getCita().getId());
            Optional<Mascota> mascota = mascotaRepository.findById(historialClinico.getMascota().getId());
            
            if (cita.isEmpty() || mascota.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            historialClinico.setCita(cita.get());
            historialClinico.setMascota(mascota.get());
            
            Historial_clinico nuevoHistorialClinico = historialClinicoRepository.save(historialClinico);
            return new ResponseEntity<>(nuevoHistorialClinico, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar historial clínico
    @PutMapping("/HistorialClinico/{id}")
    public ResponseEntity<Historial_clinico> update(@PathVariable("id") Long id, @RequestBody Historial_clinico historialClinico) {
        Optional<Historial_clinico> historialClinicoData = historialClinicoRepository.findById(id);

        if (historialClinicoData.isPresent()) {
            Historial_clinico _historialClinico = historialClinicoData.get();
            
            // Actualizar cita si se proporciona
            if (historialClinico.getCita() != null && historialClinico.getCita().getId() != null) {
                Optional<Cita> cita = citaRepository.findById(historialClinico.getCita().getId());
                cita.ifPresent(_historialClinico::setCita);
            }
            
            // Actualizar mascota si se proporciona
            if (historialClinico.getMascota() != null && historialClinico.getMascota().getId() != null) {
                Optional<Mascota> mascota = mascotaRepository.findById(historialClinico.getMascota().getId());
                mascota.ifPresent(_historialClinico::setMascota);
            }
            
            _historialClinico.setFecha(historialClinico.getFecha());
            _historialClinico.setPeso(historialClinico.getPeso());
            _historialClinico.setDiagnostico(historialClinico.getDiagnostico());
            _historialClinico.setTratamiento(historialClinico.getTratamiento());
            _historialClinico.setObservaciones(historialClinico.getObservaciones());

            return new ResponseEntity<>(historialClinicoRepository.save(_historialClinico), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar historial clínico
    @DeleteMapping("/HistorialClinico/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
        try {
            historialClinicoRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}  