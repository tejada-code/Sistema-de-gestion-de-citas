package com.utp.integradorspringboot.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.utp.integradorspringboot.models.Rol;
import com.utp.integradorspringboot.repositories.RolRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class RolController {
    @Autowired
    RolRepository rolRepository;

    @GetMapping("/Rol")
    public ResponseEntity<List<Rol>> getAll() {
        try {
            List<Rol> lista = new ArrayList<>();
            rolRepository.findAll().forEach(lista::add);
            if (lista.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(lista, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/Rol/{id}")
    public ResponseEntity<Rol> getById(@PathVariable("id") Long id) {
        Optional<Rol> entidad = rolRepository.findById(id);
        return entidad.map(rol -> new ResponseEntity<>(rol, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/Rol")
    public ResponseEntity<Rol> create(@RequestBody Rol rol) {
        try {
            Rol nuevo = rolRepository.save(rol);
            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/Rol/{id}")
    public ResponseEntity<Rol> update(@PathVariable("id") Long id, @RequestBody Rol rol) {
        Optional<Rol> entidad = rolRepository.findById(id);
        if (entidad.isPresent()) {
            Rol _rol = entidad.get();
            _rol.setNombre(rol.getNombre());
            return new ResponseEntity<>(rolRepository.save(_rol), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/Rol/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
        try {
            rolRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} 