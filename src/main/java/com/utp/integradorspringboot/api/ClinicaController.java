package com.utp.integradorspringboot.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.utp.integradorspringboot.models.Clinica;
import com.utp.integradorspringboot.models.Empleado_clinica;
import com.utp.integradorspringboot.models.Usuario;
import com.utp.integradorspringboot.repositories.ClinicaRepository;
import com.utp.integradorspringboot.repositories.EmpleadoClinicaRepository;
import com.utp.integradorspringboot.repositories.UsuarioRepository;
import com.utp.integradorspringboot.services.AuthService;

import jakarta.servlet.http.HttpSession;

@CrossOrigin(origins = "http://localhost:8081")
@Controller
@RequestMapping("")
public class ClinicaController {

    @Autowired
    ClinicaRepository clinicaRepository;
    @Autowired
    EmpleadoClinicaRepository empleadoClinicaRepository;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    private AuthService authService;

    // Obtener todas las clínicas
    @GetMapping("/Clinica")
    public ResponseEntity<List<Clinica>> getAll() {
        try {
            List<Clinica> lista = new ArrayList<>();
            clinicaRepository.findAll().forEach(lista::add);

            if (lista.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(lista, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());                  
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener clínica por ID
    @GetMapping("/Clinica/{id}")
    public ResponseEntity<Clinica> getById(@PathVariable("id") Long id) {
        Optional<Clinica> clinicaData = clinicaRepository.findById(id);

        if (clinicaData.isPresent()) {
            return new ResponseEntity<>(clinicaData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Crear nueva clínica
    @PostMapping("/Clinica")
    public ResponseEntity<Clinica> create(@RequestBody Clinica clinica) {
        try {
            Clinica nuevaClinica = clinicaRepository.save(clinica);
            return new ResponseEntity<>(nuevaClinica, HttpStatus.CREATED);

        } catch (Exception e) {
            System.out.println(e.getMessage());                        
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar clínica
    @PutMapping("/Clinica/{id}")
    public ResponseEntity<Clinica> update(@PathVariable("id") Long id, @RequestBody Clinica clinica) {
        Optional<Clinica> clinicaData = clinicaRepository.findById(id);

        if (clinicaData.isPresent()) {
            Clinica _clinica = clinicaData.get();
            _clinica.setNombre_clinica(clinica.getNombre_clinica());
            _clinica.setRuc(clinica.getRuc());
            _clinica.setDireccion(clinica.getDireccion());
            _clinica.setTelefono(clinica.getTelefono());
            _clinica.setLink_web(clinica.getLink_web());
            _clinica.setPlan_suscripcion(clinica.getPlan_suscripcion());
            _clinica.setPasarela_pago(clinica.getPasarela_pago());

            return new ResponseEntity<>(clinicaRepository.save(_clinica), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar clínica
    @DeleteMapping("/Clinica/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
        try {
            clinicaRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            System.out.println(e.getMessage());                        
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/administrador/mi-clinica")
    public String mostrarMiClinica(HttpSession session, Model model) {
        if (!authService.isLoggedIn(session)) {
            return "redirect:/login";
        }
        Usuario usuario = authService.getCurrentUser(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        Empleado_clinica empleado = empleadoClinicaRepository.findByUsuario_IdAndTipoEmpleado(usuario.getId(), "Administrador");
        if (empleado == null) {
            model.addAttribute("error", "No se encontró la clínica asociada a este administrador.");
            return "administrador/mi-clinica";
        }
        Clinica clinica = empleado.getClinica();
        model.addAttribute("clinica", clinica);
        return "administrador/mi-clinica";
    }
} 