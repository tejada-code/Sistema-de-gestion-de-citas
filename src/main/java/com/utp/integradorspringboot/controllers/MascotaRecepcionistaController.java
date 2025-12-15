package com.utp.integradorspringboot.controllers;

import com.utp.integradorspringboot.models.Dueno;
import com.utp.integradorspringboot.models.Mascota;
import com.utp.integradorspringboot.repositories.DuenoRepository;
import com.utp.integradorspringboot.repositories.MascotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class MascotaRecepcionistaController {
    @Autowired
    private MascotaRepository mascotaRepository;
    @Autowired
    private DuenoRepository duenoRepository;

    @GetMapping("/recepcionista/mascotas")
    public String gestionarMascotas(Model model) {
        return "recepcionista/gestionar_mascotas";
    }

    @PostMapping("/recepcionista/mascotas/enroll")
    @ResponseBody
    public Map<String, Object> enrollMascota(@RequestBody Map<String, Object> data) {
        Map<String, Object> resp = new HashMap<>();
        try {
            String nombre = (String) data.get("nombre");
            String especie = (String) data.get("especie");
            String raza = (String) data.get("raza");
            Integer edad = data.get("edad") != null ? Integer.parseInt(data.get("edad").toString()) : null;
            Map duenoMap = (Map) data.get("dueno");
            if (nombre == null || nombre.trim().isEmpty() || especie == null || raza == null || edad == null || duenoMap == null) {
                resp.put("success", false);
                resp.put("error", "Todos los campos son obligatorios");
                return resp;
            }
            Long duenoId = Long.parseLong(duenoMap.get("id").toString());
            Optional<Dueno> duenoOpt = duenoRepository.findById(duenoId);
            if (duenoOpt.isEmpty()) {
                resp.put("success", false);
                resp.put("error", "Dueño no encontrado");
                return resp;
            }
            Mascota mascota = new Mascota();
            mascota.setNombre(nombre);
            mascota.setEspecie(especie);
            mascota.setRaza(raza);
            mascota.setEdad(edad);
            mascota.setDueno(duenoOpt.get());
            mascotaRepository.save(mascota);
            resp.put("success", true);
            resp.put("mascota", mascota);
        } catch (Exception e) {
            resp.put("success", false);
            resp.put("error", e.getMessage());
        }
        return resp;
    }
} 