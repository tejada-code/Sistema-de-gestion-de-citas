package com.utp.integradorspringboot.controllers;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.utp.integradorspringboot.models.Dueno;
import com.utp.integradorspringboot.models.Recepcionista;
import com.utp.integradorspringboot.models.Sesion;
import com.utp.integradorspringboot.models.Usuario;
import com.utp.integradorspringboot.models.Veterinario;
import com.utp.integradorspringboot.repositories.DuenoRepository;
import com.utp.integradorspringboot.repositories.RecepcionistaRepository;
import com.utp.integradorspringboot.repositories.SesionRepository;
import com.utp.integradorspringboot.repositories.UsuarioRepository;
import com.utp.integradorspringboot.repositories.VeterinarioRepository;
import com.utp.integradorspringboot.services.AuthService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private SesionRepository sesionRepository;
    
    @Autowired
    private DuenoRepository duenoRepository;
    @Autowired
    private RecepcionistaRepository recepcionistaRepository;
    @Autowired
    private VeterinarioRepository veterinarioRepository;
    
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String email, 
                       @RequestParam String password, 
                       HttpSession session, 
                       RedirectAttributes redirectAttributes) {
        
        if (authService.login(email, password, session)) {
            Usuario usuario = authService.getCurrentUser(session);
            AuthService.UserType userType = authService.getCurrentUserType(session);
            
            // Redirigir según el tipo de usuario
            switch (userType) {
                case DUENO:
                    return "redirect:/dueno/dashboard";
                case VETERINARIO:
                    return "redirect:/veterinario/dashboard";
                case ADMINISTRADOR:
                    return "redirect:/administrador/dashboard";
                case RECEPCIONISTA:
                    return "redirect:/recepcionista/dashboard";
                default:
                    return "redirect:/";
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Credenciales inválidas");
            return "redirect:/login";
        }
    }
    
    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }
    
    @PostMapping("/register")
    public String register(@RequestParam String nombres,
                          @RequestParam String apellidos,
                          @RequestParam String dni,
                          @RequestParam String celular,
                          @RequestParam String email,
                          @RequestParam String password,
                          RedirectAttributes redirectAttributes) {

        // Normalizar email
        String normalizedEmail = email.trim().toLowerCase();
        // Verificar si el email ya existe
        if (sesionRepository.findByCorreo(normalizedEmail).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "El email ya está registrado");
            return "redirect:/register";
        }

        // Crear nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setNombres(nombres);
        usuario.setApellidos(apellidos);
        usuario.setDni(dni);
        usuario.setCelular(celular);
        usuario.setFecha_registro(LocalDateTime.now());
        usuario = usuarioRepository.save(usuario);

        // Crear sesión para el usuario

        Sesion sesion = new Sesion();
        sesion.setCorreo(normalizedEmail);
        sesion.setContrasena(password);
        sesion.setUsuario(usuario);
        sesion.setFecha_creacion(LocalDateTime.now());
        sesionRepository.save(sesion);

        // Crear entidad según el tipo de usuario
        AuthService.UserType userType = authService.getUserType(normalizedEmail);
        switch (userType) {
            case VETERINARIO:
                Veterinario veterinario = new Veterinario();
                veterinario.setUsuario(usuario);
                veterinario.setNumero_colegio_medico("VET" + usuario.getId());
                veterinario.setEspecialidad("General");
                veterinarioRepository.save(veterinario);
                break;
            case DUENO:
                Dueno dueno = new Dueno();
                dueno.setUsuario(usuario);
                duenoRepository.save(dueno);
                break;
            case RECEPCIONISTA:
                Recepcionista recepcionista = new Recepcionista();
                recepcionista.setUsuario(usuario);
                recepcionistaRepository.save(recepcionista);
                break;
            case ADMINISTRADOR:
                // Implement if you have an Administrador entity
                break;
            default:
                // No extra entity needed
                break;
        }

        redirectAttributes.addFlashAttribute("success", "Usuario registrado exitosamente");
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Cierra la sesión
        return "redirect:/login"; // Redirige al login
    }
} 