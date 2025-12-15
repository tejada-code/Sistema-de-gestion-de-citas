package com.utp.integradorspringboot.api;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.utp.integradorspringboot.dto.PersonalDTO;
import com.utp.integradorspringboot.services.GestionPersonalService;

/**
 * Controlador API para la gestión de personal
 * Combina funcionalidad de Veterinario y Recepcionista
 */
@RestController
@RequestMapping("/api/gestion-personal")
@CrossOrigin(origins = "http://localhost:8081")
public class GestionPersonalApiController {

    @Autowired
    private GestionPersonalService gestionPersonalService;

    /**
     * Obtiene todo el personal
     */
    @GetMapping("/personal")
    public ResponseEntity<List<PersonalDTO>> getAllPersonal() {
        try {
            List<PersonalDTO> personalList = gestionPersonalService.getAllPersonal();
            if (personalList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(personalList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene personal por ID
     */
    @GetMapping("/personal/{id}")
    public ResponseEntity<PersonalDTO> getPersonalById(@PathVariable("id") Long id, 
                                                     @RequestParam("tipo") String tipo) {
        try {
            PersonalDTO personal = gestionPersonalService.getPersonalById(id, tipo);
            if (personal != null) {
                return new ResponseEntity<>(personal, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Crea nuevo personal
     */
    @PostMapping("/personal")
    public ResponseEntity<PersonalDTO> createPersonal(@RequestBody PersonalDTO personalDTO) {
        try {
            PersonalDTO createdPersonal = gestionPersonalService.createPersonal(personalDTO);
            return new ResponseEntity<>(createdPersonal, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Actualiza personal existente
     */
    @PutMapping("/personal/{id}")
    public ResponseEntity<PersonalDTO> updatePersonal(@PathVariable("id") Long id, 
                                                    @RequestBody PersonalDTO personalDTO) {
        try {
            PersonalDTO updatedPersonal = gestionPersonalService.updatePersonal(id, personalDTO);
            if (updatedPersonal != null) {
                return new ResponseEntity<>(updatedPersonal, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Elimina personal por ID
     */
    @DeleteMapping("/personal/{id}")
    public ResponseEntity<HttpStatus> deletePersonal(@PathVariable("id") Long id, 
                                                   @RequestParam("tipo") String tipo) {
        try {
            boolean deleted = gestionPersonalService.deletePersonal(id, tipo);
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene clínicas disponibles (para el formulario de recepcionistas)
     */
    @GetMapping("/clinicas")
    public ResponseEntity<?> getClinicas() {
        try {
            // Este endpoint se puede implementar si necesitas obtener las clínicas
            // para el formulario de recepcionistas
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} 