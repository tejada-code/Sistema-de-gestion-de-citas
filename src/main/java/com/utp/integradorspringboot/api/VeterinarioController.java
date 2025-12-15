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

import com.utp.integradorspringboot.models.Sesion;
import com.utp.integradorspringboot.models.Usuario;
import com.utp.integradorspringboot.models.Veterinario;
import com.utp.integradorspringboot.repositories.SesionRepository;
import com.utp.integradorspringboot.repositories.UsuarioRepository;
import com.utp.integradorspringboot.repositories.VeterinarioRepository;

// DTO for detailed veterinarian info
class VeterinarioDetalleDTO {
    public Long id;
    public String nombres;
    public String apellidos;
    public String especialidad;
    public String celular;
    public String email;
    public VeterinarioDetalleDTO(Long id, String nombres, String apellidos, String especialidad, String celular, String email) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.especialidad = especialidad;
        this.celular = celular;
        this.email = email;
    }
}

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class VeterinarioController {

    @Autowired
    VeterinarioRepository veterinarioRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    SesionRepository sesionRepository;

    // Obtener todos los veterinarios
    @GetMapping("/Veterinario")
    public ResponseEntity<List<Veterinario>> getAll() {
        try {
            List<Veterinario> lista = new ArrayList<>();
            veterinarioRepository.findAll().forEach(lista::add);

            if (lista.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(lista, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener todos los veterinarios (endpoint alternativo)
    @GetMapping("/veterinarios")
    public ResponseEntity<List<Veterinario>> getAllVeterinarios() {
        try {
            List<Veterinario> lista = new ArrayList<>();
            veterinarioRepository.findAll().forEach(lista::add);

            if (lista.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(lista, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener veterinario por ID
    @GetMapping("/Veterinario/{id}")
    public ResponseEntity<Veterinario> getById(@PathVariable("id") Long id) {
        Optional<Veterinario> veterinarioData = veterinarioRepository.findById(id);

        if (veterinarioData.isPresent()) {
            return new ResponseEntity<>(veterinarioData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Crear nuevo veterinario
    @PostMapping("/Veterinario")
    public ResponseEntity<Veterinario> create(@RequestBody Veterinario veterinario) {
        try {
            // Validar que el usuario exista
            Optional<Usuario> usuario = usuarioRepository.findById(veterinario.getUsuario().getId());
            if (usuario.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            veterinario.setUsuario(usuario.get());
            Veterinario nuevoVeterinario = veterinarioRepository.save(veterinario);
            return new ResponseEntity<>(nuevoVeterinario, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar veterinario
    @PutMapping("/Veterinario/{id}")
    public ResponseEntity<Veterinario> update(@PathVariable("id") Long id, @RequestBody Veterinario veterinario) {
        Optional<Veterinario> veterinarioData = veterinarioRepository.findById(id);

        if (veterinarioData.isPresent()) {
            Veterinario _veterinario = veterinarioData.get();
            _veterinario.setNumero_colegio_medico(veterinario.getNumero_colegio_medico());
            _veterinario.setEspecialidad(veterinario.getEspecialidad());

            // Opcional: actualizar usuario si lo necesitas
            if (veterinario.getUsuario() != null && veterinario.getUsuario().getId() != null) {
                Optional<Usuario> usuario = usuarioRepository.findById(veterinario.getUsuario().getId());
                usuario.ifPresent(_veterinario::setUsuario);
            }

            return new ResponseEntity<>(veterinarioRepository.save(_veterinario), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar veterinario
    @DeleteMapping("/Veterinario/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
        try {
            veterinarioRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/veterinarios/detalle")
    public ResponseEntity<List<VeterinarioDetalleDTO>> getVeterinariosDetalle() {
        try {
            List<Veterinario> lista = veterinarioRepository.findAll();
            List<VeterinarioDetalleDTO> dtos = new ArrayList<>();
            for (Veterinario v : lista) {
                String nombres = v.getUsuario() != null ? v.getUsuario().getNombres() : null;
                String apellidos = v.getUsuario() != null ? v.getUsuario().getApellidos() : null;
                String celular = v.getUsuario() != null ? v.getUsuario().getCelular() : null;
                String especialidad = v.getEspecialidad();
                String email = null;
                if (v.getUsuario() != null) {
                    Sesion sesion = sesionRepository.findByUsuario_Id(v.getUsuario().getId()).orElse(null);
                    if (sesion != null) email = sesion.getCorreo();
                }
                dtos.add(new VeterinarioDetalleDTO(v.getId(), nombres, apellidos, especialidad, celular, email));
            }
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} 
