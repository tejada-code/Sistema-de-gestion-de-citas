package com.utp.integradorspringboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador web para la gestión de personal
 * Maneja las vistas del sistema de gestión de personal
 */
@Controller
@RequestMapping("/administrador/gestion-personal")
public class GestionPersonalController {
    
    @GetMapping
    public String gestionPersonal(Model model) {
        model.addAttribute("titulo", "Gestión de Personal");
        model.addAttribute("subtitulo", "Administración del personal veterinario y recepcionistas");
        return "administrador/gestion-personal";
    }
} 