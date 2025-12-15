package com.utp.integradorspringboot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.utp.integradorspringboot.services.PasswordResetService;

@Controller
public class PasswordResetController {
    
    @Autowired
    private PasswordResetService passwordResetService;
    
    @GetMapping("/simple-test")
    @ResponseBody
    public String simpleTest() {
        return "Simple test endpoint working!";
    }
    
    @GetMapping("/test-forgot-password")
    public String testEndpoint() {
        System.out.println("DEBUG: Test endpoint accessed successfully");
        return "auth/forgot-password";
    }
    
    @GetMapping("/health-check")
    @ResponseBody
    public String healthCheck() {
        System.out.println("DEBUG: Health check endpoint accessed");
        return "Health check successful - Application is running";
    }
    
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        System.out.println("DEBUG: Accessing forgot-password GET endpoint");
        return "auth/forgot-password";
    }
    
    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, 
                                      RedirectAttributes redirectAttributes) {
        System.out.println("DEBUG: Processing forgot-password POST request for email: " + email);
        try {
            boolean success = passwordResetService.requestPasswordReset(email);
            
            if (success) {
                redirectAttributes.addFlashAttribute("message", 
                    "Se ha enviado un enlace de restablecimiento a tu correo electrónico.");
                redirectAttributes.addFlashAttribute("messageType", "success");
            } else {
                redirectAttributes.addFlashAttribute("message", 
                    "No se encontró una cuenta con ese correo electrónico.");
                redirectAttributes.addFlashAttribute("messageType", "error");
            }
            
            return "redirect:/forgot-password";
        } catch (Exception e) {
            System.err.println("ERROR in processForgotPassword: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", 
                "Error interno del servidor. Inténtalo de nuevo.");
            redirectAttributes.addFlashAttribute("messageType", "error");
            return "redirect:/forgot-password";
        }
    }
    
    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam String token, Model model) {
        System.out.println("DEBUG: Accessing reset-password GET endpoint with token: " + token);
        try {
            boolean validToken = passwordResetService.validateToken(token);
            
            if (!validToken) {
                model.addAttribute("error", "El enlace de restablecimiento no es válido o ha expirado.");
                return "auth/reset-password";
            }
            
            model.addAttribute("token", token);
            return "auth/reset-password";
        } catch (Exception e) {
            System.err.println("ERROR in showResetPasswordForm: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error interno del servidor.");
            return "auth/reset-password";
        }
    }
    
    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam String token,
                                     @RequestParam String password,
                                     @RequestParam String confirmPassword,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {
        System.out.println("DEBUG: Processing reset-password POST request");
        try {

            // Validar token

            if (!passwordResetService.validateToken(token)) {
                model.addAttribute("error", "El enlace de restablecimiento no es válido o ha expirado.");
                model.addAttribute("token", token);
                return "auth/reset-password";
            }
            
            // Validar que las contraseñas coincidan

            if (!password.equals(confirmPassword)) {
                model.addAttribute("error", "Las contraseñas no coinciden.");
                model.addAttribute("token", token);
                return "auth/reset-password";
            }
            
            // Validar longitud de contraseña

            if (password.length() < 6) {
                model.addAttribute("error", "La contraseña debe tener al menos 6 caracteres.");
                model.addAttribute("token", token);
                return "auth/reset-password";
            }
            
            // Restablecer contraseña

            boolean success = passwordResetService.resetPassword(token, password);
            
            if (success) {
                redirectAttributes.addFlashAttribute("message", 
                    "Tu contraseña ha sido restablecida exitosamente. Puedes iniciar sesión ahora.");
                redirectAttributes.addFlashAttribute("messageType", "success");
                return "redirect:/login";
            } else {
                model.addAttribute("error", "Error al restablecer la contraseña. Inténtalo de nuevo.");
                model.addAttribute("token", token);
                return "auth/reset-password";
            }
        } catch (Exception e) {
            System.err.println("ERROR in processResetPassword: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error interno del servidor.");
            model.addAttribute("token", token);
            return "auth/reset-password";
        }
    }
} 