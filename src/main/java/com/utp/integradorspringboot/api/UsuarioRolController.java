package com.utp.integradorspringboot.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.utp.integradorspringboot.models.Usuario_rol;
import com.utp.integradorspringboot.models.Usuario;
import com.utp.integradorspringboot.models.Rol;
import com.utp.integradorspringboot.repositories.UsuarioRolRepository;
import com.utp.integradorspringboot.repositories.UsuarioRepository;
import com.utp.integradorspringboot.repositories.RolRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class UsuarioRolController {
    @Autowired
    UsuarioRolRepository usuarioRolRepository;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    RolRepository rolRepository;

    @GetMapping("/UsuarioRol")
    public ResponseEntity<List<Usuario_rol>> getAll() {
        try {
            List<Usuario_rol> lista = new ArrayList<>();
            usuarioRolRepository.findAll().forEach(lista::add);
            if (lista.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(lista, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/UsuarioRol/{id}")
    public ResponseEntity<Usuario_rol> getById(@PathVariable("id") Long id) {
        Optional<Usuario_rol> entidad = usuarioRolRepository.findById(id);
        return entidad.map(usuarioRol -> new ResponseEntity<>(usuarioRol, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/UsuarioRol")
    public ResponseEntity<Usuario_rol> create(@RequestBody Usuario_rol usuarioRol) {
        try {
            if (usuarioRol.getUsuario() == null || usuarioRol.getUsuario().getId() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if (usuarioRol.getRol() == null || usuarioRol.getRol().getId() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            Optional<Usuario> usuario = usuarioRepository.findById(usuarioRol.getUsuario().getId());
            Optional<Rol> rol = rolRepository.findById(usuarioRol.getRol().getId());
            if (usuario.isEmpty() || rol.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            usuarioRol.setUsuario(usuario.get());
            usuarioRol.setRol(rol.get());
            Usuario_rol nuevo = usuarioRolRepository.save(usuarioRol);
            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/UsuarioRol/{id}")
    public ResponseEntity<Usuario_rol> update(@PathVariable("id") Long id, @RequestBody Usuario_rol usuarioRol) {
        Optional<Usuario_rol> entidad = usuarioRolRepository.findById(id);
        if (entidad.isPresent()) {
            Usuario_rol _usuarioRol = entidad.get();
            if (usuarioRol.getUsuario() != null && usuarioRol.getUsuario().getId() != null) {
                Optional<Usuario> usuario = usuarioRepository.findById(usuarioRol.getUsuario().getId());
                usuario.ifPresent(_usuarioRol::setUsuario);
            }
            if (usuarioRol.getRol() != null && usuarioRol.getRol().getId() != null) {
                Optional<Rol> rol = rolRepository.findById(usuarioRol.getRol().getId());
                rol.ifPresent(_usuarioRol::setRol);
            }
            return new ResponseEntity<>(usuarioRolRepository.save(_usuarioRol), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/UsuarioRol/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
        try {
            usuarioRolRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} 