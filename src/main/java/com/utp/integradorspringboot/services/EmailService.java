/*
 * Plantilla de licencia generada por NetBeans. Puedes modificarla según las necesidades del sistema.
 */
package com.utp.integradorspringboot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.utp.integradorspringboot.models.Email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 *
 * @author jcerv_pm92n0w
 */
@Service
public class EmailService {
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Autowired
    private JavaMailSender mailSender;
    
    public static void SolicitarEnvio(Email email, JavaMailSender emailSender) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("pruebagestioncita@gmail.com");
            message.setTo(email.getTo());
            message.setSubject(email.getSubject());
            message.setText(email.getMessage());
            emailSender.send(message);
        } catch (Exception e) {
            // Registrar error
        }
    }

    public void sendHtmlEmail(String to, String subject, String htmlContent, JavaMailSender emailSender) throws MessagingException {
        System.out.println("DEBUG: Starting sendHtmlEmail to: " + to + ", subject: " + subject);
        try {
            JavaMailSender sender = emailSender != null ? emailSender : mailSender;
            System.out.println("DEBUG: Using mailSender: " + (sender != null ? "available" : "null"));
            
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            
            System.out.println("DEBUG: Sending email from: " + fromEmail + " to: " + to);
            sender.send(message);
            System.out.println("DEBUG: Email sent successfully");
        } catch (MessagingException e) {
            System.err.println("ERROR in sendHtmlEmail: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public void sendPasswordResetEmail(String to, String resetLink, JavaMailSender emailSender) throws MessagingException {
        System.out.println("DEBUG: Starting sendPasswordResetEmail to: " + to);
        String subject = "Restablecimiento de contraseña";
        String htmlContent = """
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                    <div style="background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0;">
                        <h1 style="margin: 0; font-size: 24px;">Restablecimiento de Contraseña</h1>
                        <p style="margin: 10px 0 0 0; opacity: 0.9;">UTP VetGes - Sistema de Gestión Clínicas Veterinarias</p>
                    </div>
                    <div style="background: #f8f9fa; padding: 30px; border-radius: 0 0 10px 10px;">
                        <h2 style="color: #667eea; margin-top: 0;">Hola,</h2>
                        <p>Hemos recibido una solicitud para restablecer la contraseña de tu cuenta.</p>
                        <p>Si fuiste tú quien solicitó este cambio, haz clic en el siguiente botón para restablecer tu contraseña:</p>
                        <div style="text-align: center; margin: 30px 0;">
                            <a href=\"%s\" style=\"background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); color: white; padding: 15px 30px; text-decoration: none; border-radius: 25px; display: inline-block; font-weight: bold;\">
                                Restablecer Contraseña
                            </a>
                        </div>
                        <p><strong>Importante:</strong></p>
                        <ul>
                            <li>Este enlace expirará en 24 horas</li>
                            <li>Si no solicitaste este cambio, puedes ignorar este correo</li>
                            <li>Tu contraseña actual no cambiará hasta que hagas clic en el enlace</li>
                        </ul>
                        <hr style="border: none; border-top: 1px solid #ddd; margin: 30px 0;">
                        <p style="font-size: 12px; color: #666; text-align: center;">
                            Este es un correo automático, por favor no respondas a este mensaje.
                        </p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(resetLink);
        sendHtmlEmail(to, subject, htmlContent, emailSender);
        System.out.println("DEBUG: Password reset email sent successfully to: " + to);
    }
}
