package com.utp.integradorspringboot.controllers;

import com.utp.integradorspringboot.models.Usuario;
import com.utp.integradorspringboot.models.Sesion;
import com.utp.integradorspringboot.repositories.UsuarioRepository;
import com.utp.integradorspringboot.repositories.SesionRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PerfilController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private SesionRepository sesionRepository;

    // Mostrar perfil
    @GetMapping("/perfil")
    public String mostrarPerfil(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión.");
            return "redirect:/login";
        }
        
        // Obtener el email de la sesión
        String email = "";
        var sesionOpt = sesionRepository.findByUsuario_Id(usuario.getId());
        if (sesionOpt.isPresent()) {
            email = sesionOpt.get().getCorreo();
        }
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("email", email);
        return "perfil";
    }

    // Actualizar perfil
    @PostMapping("/perfil")
    public String actualizarPerfil(@ModelAttribute Usuario usuarioActualizado, 
                                  @RequestParam String email,
                                  HttpSession session, 
                                  RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión.");
            return "redirect:/login";
        }
        
        // Actualizar celular del usuario
        usuario.setCelular(usuarioActualizado.getCelular());
        usuarioRepository.save(usuario);
        
        // Actualizar email en la sesión
        var sesionOpt = sesionRepository.findByUsuario_Id(usuario.getId());
        if (sesionOpt.isPresent()) {
            Sesion sesion = sesionOpt.get();
            sesion.setCorreo(email);
            sesionRepository.save(sesion);
        }
        
        session.setAttribute("usuario", usuario);
        return "redirect:/perfil?success=true";
    }
}
