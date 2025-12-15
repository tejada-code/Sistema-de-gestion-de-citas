package com.utp.integradorspringboot.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.utp.integradorspringboot.models.PasswordResetToken;
import com.utp.integradorspringboot.models.Sesion;
import com.utp.integradorspringboot.models.Usuario;
import com.utp.integradorspringboot.repositories.PasswordResetTokenRepository;
import com.utp.integradorspringboot.repositories.SesionRepository;
import com.utp.integradorspringboot.repositories.UsuarioRepository;

import jakarta.mail.MessagingException;

@Service
@Transactional
public class PasswordResetService {
    // Servicio para gestionar el reseteo de contraseñas
    
    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    
    @Autowired
    private SesionRepository sesionRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private EmailService emailService;
    
    @Transactional
    public boolean requestPasswordReset(String email) {
        // Solicita el reseteo de contraseña para el email dado
        System.out.println("DEBUG: Starting requestPasswordReset for email: " + email);
        try {

            // Normalizar email
            String normalizedEmail = email.trim().toLowerCase();
            // Depurar: Imprimir todos los emails en la base de datos
            sesionRepository.findAll().forEach(s -> System.out.println("DB email: [" + s.getCorreo() + "]"));
            // Depurar: Imprimir el email normalizado que se está buscando
            System.out.println("Normalized email being searched: [" + normalizedEmail + "]");
            Optional<Sesion> sesionOpt = sesionRepository.findByCorreoIgnoreCase(normalizedEmail);
            if (sesionOpt.isEmpty()) {
                System.out.println("DEBUG: No session found for email: " + normalizedEmail);
                return false; // Email no encontrado

            }
            
            Sesion sesion = sesionOpt.get();
            Long userId = sesion.getUsuario().getId();
            System.out.println("DEBUG: Found user with ID: " + userId);
            

            // Eliminar cualquier token existente para este usuario

            System.out.println("DEBUG: Deleting existing tokens for user: " + userId);
            List<PasswordResetToken> existingTokens = tokenRepository.findByUserId(userId);
            tokenRepository.deleteAll(existingTokens);
            

            // Generar nuevo token
            String token = UUID.randomUUID().toString();
            LocalDateTime expirationDate = LocalDateTime.now().plusHours(24); // 24 horas de expiración

            System.out.println("DEBUG: Generated token: " + token);
            
            PasswordResetToken resetToken = new PasswordResetToken(userId, token, expirationDate);
            tokenRepository.save(resetToken);
            System.out.println("DEBUG: Saved reset token to database");
            

            // Enviar email

            try {
                String resetLink = "http://localhost:8081/reset-password?token=" + token;
                System.out.println("DEBUG: Sending email with reset link: " + resetLink);
                emailService.sendPasswordResetEmail(normalizedEmail, resetLink, null);
                System.out.println("DEBUG: Email sent successfully");
            } catch (MessagingException emailException) {

                // Registrar error de email pero no fallar toda la solicitud
                System.err.println("Failed to send password reset email: " + emailException.getMessage());
                System.out.println("DEBUG: Email sending failed, but continuing with process");
                // Continuar con el proceso incluso si falla el email

            }
            
            System.out.println("DEBUG: Password reset request completed successfully");
            return true;
        } catch (Exception e) {
            System.err.println("ERROR in requestPasswordReset: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Transactional(readOnly = true)
    public boolean validateToken(String token) {
        // Valida si el token es válido y no ha expirado
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);
        if (tokenOpt.isEmpty()) {
            return false;
        }
        
        PasswordResetToken resetToken = tokenOpt.get();
        return !resetToken.getUsed() && !resetToken.isExpired();
    }
    
    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        // Cambia la contraseña usando el token
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);
        if (tokenOpt.isEmpty()) {
            return false;
        }
        
        PasswordResetToken resetToken = tokenOpt.get();
        if (resetToken.getUsed() || resetToken.isExpired()) {
            return false;
        }
        
        try {

            // Encontrar el usuario

            Optional<Usuario> usuarioOpt = usuarioRepository.findById(resetToken.getUserId());
            if (usuarioOpt.isEmpty()) {
                return false;
            }
            

            // Encontrar la sesión por ID de usuario (necesitamos agregar este método al repositorio)
            // Por ahora, usaremos un enfoque diferente - buscar por usuario

            List<Sesion> sesiones = sesionRepository.findAll();
            Optional<Sesion> sesionOpt = sesiones.stream()
                .filter(s -> s.getUsuario().getId().equals(resetToken.getUserId()))
                .findFirst();
            
            if (sesionOpt.isEmpty()) {
                return false;
            }
            
            Sesion sesion = sesionOpt.get();
            sesion.setContrasena(newPassword);
            sesionRepository.save(sesion);
            

            // Marcar token como usado

            resetToken.setUsed(true);
            tokenRepository.save(resetToken);
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Transactional
    public void cleanupExpiredTokens() {
        // Elimina tokens expirados
        tokenRepository.deleteExpiredTokens(LocalDateTime.now());
    }
} 