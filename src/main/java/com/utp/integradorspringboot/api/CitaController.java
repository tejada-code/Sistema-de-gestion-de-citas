
package com.utp.integradorspringboot.api;

import com.utp.integradorspringboot.models.Cita;
import com.utp.integradorspringboot.models.Detalle_cita;
import com.utp.integradorspringboot.models.Motivo_cita;
import com.utp.integradorspringboot.repositories.GestionCitaRepository;
import com.utp.integradorspringboot.repositories.DetalleCitaRepository;
import com.utp.integradorspringboot.repositories.MotivoCitaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

/**
 *
 * @author USER
 */
@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class CitaController {
    @Autowired
    GestionCitaRepository repository;
    
    @Autowired
    DetalleCitaRepository detalleCitaRepository;
    
    @Autowired
    MotivoCitaRepository motivoCitaRepository;

    @GetMapping("/Cita")
    public ResponseEntity<List<Cita>> getAll(@RequestParam(required = false) String title) {
        try {
            List<Cita> lista = new ArrayList<Cita>();
            repository.findAll().forEach(lista::add);
            
            // Cargar las relaciones explícitamente para evitar problemas de lazy loading
            for (Cita cita : lista) {
                // Forzar la carga de las relaciones asociadas a la cita
                if (cita.getVeterinario() != null) {
                    cita.getVeterinario().getUsuario();
                }
                if (cita.getDueno() != null) {
                    cita.getDueno().getUsuario();
                }
                if (cita.getMascota() != null) {
                    cita.getMascota().getDueno();
                }
                if (cita.getDetalleCita() != null && cita.getDetalleCita().getMotivo_cita() != null) {
                    cita.getDetalleCita().getMotivo_cita().getNombre();
                }
            }
            
            System.out.println("Total citas encontradas: " + lista.size());
            for (Cita cita : lista) {
                System.out.println("Cita ID: " + cita.getId() + 
                                 ", Fecha: " + cita.getFecha() + 
                                 ", Veterinario: " + (cita.getVeterinario() != null ? cita.getVeterinario().getId() : "null") +
                                 ", Dueno: " + (cita.getDueno() != null ? cita.getDueno().getId() : "null") +
                                 ", DetalleCita: " + (cita.getDetalleCita() != null ? "Sí" : "No"));
            }
            
            if (lista.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(lista, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/Cita/{id}")
    public ResponseEntity<Cita> getById(@PathVariable("id") Long id) {
        Optional<Cita> entidad = repository.findById(id);
        if (entidad.isPresent()) {
            return new ResponseEntity<>(entidad.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/Cita")
    public ResponseEntity<Cita> create(@RequestBody Cita entidad) {
        try {
            // Crear la cita principal
            Cita _entidad = repository.save(new Cita(
                null, 
                entidad.getFecha(),
                entidad.getHora(), 
                entidad.getEstado(),
                entidad.getObservaciones() != null ? entidad.getObservaciones() : "",
                entidad.getMascota(),
                entidad.getVeterinario(),
                entidad.getDueno()));
            
            // Si hay detalle de cita, crearlo también
            if (entidad.getDetalleCita() != null && entidad.getDetalleCita().getMotivo_cita() != null) {
                // Obtener el motivo de cita seleccionado
                Motivo_cita motivoCita = motivoCitaRepository.findById(
                    entidad.getDetalleCita().getMotivo_cita().getId()
                ).orElse(null);
                
                if (motivoCita != null) {
                    // Crear el detalle de cita asociado
                    Detalle_cita detalleCita = new Detalle_cita();
                    detalleCita.setCita(_entidad);
                    detalleCita.setMotivo_cita(motivoCita);
                    detalleCita.setEstado("Pendiente");
                    
                    detalleCitaRepository.save(detalleCita);
                }
            }
            
            return new ResponseEntity<>(_entidad, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/Cita/{id}")
    public ResponseEntity<Cita> update(@PathVariable("id") Long id, @RequestBody Cita entidad) {
        try {
            Optional<Cita> citaData = repository.findById(id);
            if (citaData.isPresent()) {
                Cita _cita = citaData.get();
                _cita.setFecha(entidad.getFecha());
                _cita.setHora(entidad.getHora());
                _cita.setEstado(entidad.getEstado());
                _cita.setObservaciones(entidad.getObservaciones());
                _cita.setMascota(entidad.getMascota());
                _cita.setVeterinario(entidad.getVeterinario());
                _cita.setDueno(entidad.getDueno());
                
                return new ResponseEntity<>(repository.save(_cita), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/Cita/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
        try {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}