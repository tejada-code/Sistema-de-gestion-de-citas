package com.utp.integradorspringboot.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.utp.integradorspringboot.models.Clinica;
import com.utp.integradorspringboot.models.Mascota;
import com.utp.integradorspringboot.models.MascotaClinica;
import com.utp.integradorspringboot.repositories.ClinicaRepository;
import com.utp.integradorspringboot.repositories.MascotaClinicaRepository;
import com.utp.integradorspringboot.repositories.MascotaRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/mascota-clinica")
public class MascotaClinicaController {
    @Autowired
    MascotaClinicaRepository mascotaClinicaRepository;
    @Autowired
    MascotaRepository mascotaRepository;
    @Autowired
    ClinicaRepository clinicaRepository;

    // Asignar una mascota a una clínica
    @PostMapping("/assign")
    public ResponseEntity<MascotaClinica> assignPetToClinic(@RequestParam Long mascotaId, @RequestParam Long clinicaId) {
        Optional<Mascota> mascotaOpt = mascotaRepository.findById(mascotaId);
        Optional<Clinica> clinicaOpt = clinicaRepository.findById(clinicaId);
        if (mascotaOpt.isEmpty() || clinicaOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // Verificar si ya existe
        List<MascotaClinica> existing = mascotaClinicaRepository.findAll().stream()
            .filter(mc -> mc.getMascota().getId().equals(mascotaId) && mc.getClinica().getId().equals(clinicaId))
            .collect(Collectors.toList());
        if (!existing.isEmpty()) {
            return new ResponseEntity<>(existing.get(0), HttpStatus.OK);
        }
        MascotaClinica mc = new MascotaClinica(mascotaOpt.get(), clinicaOpt.get(), LocalDateTime.now());
        MascotaClinica saved = mascotaClinicaRepository.save(mc);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Listar todas las clínicas para una mascota
    @GetMapping("/clinics/{mascotaId}")
    public ResponseEntity<List<Clinica>> getClinicsForPet(@PathVariable Long mascotaId) {
        List<MascotaClinica> list = mascotaClinicaRepository.findAll().stream()
            .filter(mc -> mc.getMascota().getId().equals(mascotaId))
            .collect(Collectors.toList());
        List<Clinica> clinics = list.stream().map(MascotaClinica::getClinica).collect(Collectors.toList());
        return new ResponseEntity<>(clinics, HttpStatus.OK);
    }

    // Listar todas las mascotas para una clínica
    @GetMapping("/pets/{clinicaId}")
    public ResponseEntity<List<Mascota>> getPetsForClinic(@PathVariable Long clinicaId) {
        List<MascotaClinica> list = mascotaClinicaRepository.findAll().stream()
            .filter(mc -> mc.getClinica().getId().equals(clinicaId))
            .collect(Collectors.toList());
        List<Mascota> pets = list.stream().map(MascotaClinica::getMascota).collect(Collectors.toList());
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    // Remover una mascota de una clínica
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removePetFromClinic(@RequestParam Long mascotaId, @RequestParam Long clinicaId) {
        List<MascotaClinica> list = mascotaClinicaRepository.findAll().stream()
            .filter(mc -> mc.getMascota().getId().equals(mascotaId) && mc.getClinica().getId().equals(clinicaId))
            .collect(Collectors.toList());
        if (list.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        mascotaClinicaRepository.deleteAll(list);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
} 