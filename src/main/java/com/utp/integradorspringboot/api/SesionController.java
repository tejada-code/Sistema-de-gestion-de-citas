package com.utp.integradorspringboot.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.utp.integradorspringboot.models.Sesion;
import com.utp.integradorspringboot.models.Usuario;
import com.utp.integradorspringboot.repositories.SesionRepository;
import com.utp.integradorspringboot.repositories.UsuarioRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class SesionController {
    @Autowired
    SesionRepository sesionRepository;
    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping("/Sesion")
    public ResponseEntity<List<Sesion>> getAll() {
        try {
            List<Sesion> lista = new ArrayList<>();
            sesionRepository.findAll().forEach(lista::add);
            if (lista.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(lista, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/Sesion/{id}")
    public ResponseEntity<Sesion> getById(@PathVariable("id") Long id) {
        Optional<Sesion> entidad = sesionRepository.findById(id);
        return entidad.map(sesion -> new ResponseEntity<>(sesion, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/Sesion")
    public ResponseEntity<Sesion> create(@RequestBody Sesion sesion) {
        try {
            if (sesion.getUsuario() == null || sesion.getUsuario().getId() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            Optional<Usuario> usuario = usuarioRepository.findById(sesion.getUsuario().getId());
            if (usuario.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            sesion.setUsuario(usuario.get());
            Sesion nuevo = sesionRepository.save(sesion);
            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/Sesion/{id}")
    public ResponseEntity<Sesion> update(@PathVariable("id") Long id, @RequestBody Sesion sesion) {
        Optional<Sesion> entidad = sesionRepository.findById(id);
        if (entidad.isPresent()) {
            Sesion _sesion = entidad.get();
            if (sesion.getUsuario() != null && sesion.getUsuario().getId() != null) {
                Optional<Usuario> usuario = usuarioRepository.findById(sesion.getUsuario().getId());
                usuario.ifPresent(_sesion::setUsuario);
            }
            _sesion.setCorreo(sesion.getCorreo());
            _sesion.setContrasena(sesion.getContrasena());
            _sesion.setFecha_creacion(sesion.getFecha_creacion());
            return new ResponseEntity<>(sesionRepository.save(_sesion), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/Sesion/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
        try {
            sesionRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} 
