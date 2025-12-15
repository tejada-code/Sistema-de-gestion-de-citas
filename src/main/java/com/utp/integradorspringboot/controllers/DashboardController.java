package com.utp.integradorspringboot.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.utp.integradorspringboot.models.Cita;
import com.utp.integradorspringboot.models.Veterinario;
import com.utp.integradorspringboot.models.VeterinarioDTO;
import com.utp.integradorspringboot.repositories.BoletaPagoRepository;
import com.utp.integradorspringboot.repositories.DuenoRepository;
import com.utp.integradorspringboot.repositories.EmpleadoClinicaRepository;
import com.utp.integradorspringboot.repositories.GestionCitaRepository;
import com.utp.integradorspringboot.repositories.MascotaRepository;
import com.utp.integradorspringboot.repositories.RecepcionistaRepository;
import com.utp.integradorspringboot.repositories.SesionRepository;
import com.utp.integradorspringboot.repositories.UsuarioRepository;
import com.utp.integradorspringboot.repositories.VeterinarioRepository;
import com.utp.integradorspringboot.services.AuthService;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private GestionCitaRepository citaRepository;
    
    @Autowired
    private VeterinarioRepository veterinarioRepository;
    
    @Autowired
    private SesionRepository sesionRepository;
    
    @Autowired
    private DuenoRepository duenoRepository;
    @Autowired
    private MascotaRepository mascotaRepository;
    @Autowired
    private RecepcionistaRepository recepcionistaRepository;
    @Autowired
    private EmpleadoClinicaRepository empleadoClinicaRepository;
    @Autowired
    private BoletaPagoRepository boletaPagoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    // Dashboard de Dueño
    @GetMapping("/dueno/dashboard")
    public String duenoDashboard(HttpSession session, RedirectAttributes redirectAttributes) {
        if (!authService.isLoggedIn(session)) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para acceder a esta página");
            return "redirect:/login";
        }
        
        AuthService.UserType userType = authService.getCurrentUserType(session);
        if (userType != AuthService.UserType.DUENO) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para acceder a esta página");
            return "redirect:/";
        }
        
        return "dueno/dashboard";
    }
    
    // Dashboard de Veterinario
    @GetMapping("/veterinario/dashboard")
    public String veterinarioDashboard(HttpSession session, RedirectAttributes redirectAttributes) {
        if (!authService.isLoggedIn(session)) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para acceder a esta página");
            return "redirect:/login";
        }
        
        AuthService.UserType userType = authService.getCurrentUserType(session);
        if (userType != AuthService.UserType.VETERINARIO) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para acceder a esta página");
            return "redirect:/";
        }
        
        return "veterinario/dashboard";
    }
    
    // Dashboard de Recepcionista
    @GetMapping("/recepcionista/dashboard")
    public String recepcionistaDashboard(HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        try {
            if (!authService.isLoggedIn(session)) {
                redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para acceder a esta página");
                return "redirect:/login";
            }
            
            AuthService.UserType userType = authService.getCurrentUserType(session);
            if (userType != AuthService.UserType.RECEPCIONISTA) {
                redirectAttributes.addFlashAttribute("error", "No tienes permisos para acceder a esta página");
                return "redirect:/";
            }
            
            // Obtener citas de hoy
            LocalDate today = LocalDate.now();
            List<Cita> citasHoy = citaRepository.findByFecha(today);
            System.out.println("Citas de hoy encontradas: " + citasHoy.size());
            
            // Depurar: Imprimir las primeras citas para verificar datos
            for (int i = 0; i < Math.min(citasHoy.size(), 3); i++) {
                Cita c = citasHoy.get(i);
                System.out.println("Cita " + (i+1) + ": " + 
                                 "ID=" + c.getId() + 
                                 ", Fecha=" + c.getFecha() + 
                                 ", Hora=" + c.getHora() + 
                                 ", Estado=" + c.getEstado() +
                                 ", Dueno=" + (c.getDueno() != null ? c.getDueno().getId() : "null") +
                                 ", Veterinario=" + (c.getVeterinario() != null ? c.getVeterinario().getId() : "null") +
                                 ", Mascota=" + (c.getMascota() != null ? c.getMascota().getId() : "null"));
            }
            
            // Obtener todos los veterinarios
            List<Veterinario> veterinarios = veterinarioRepository.findAllWithUsuario();
            System.out.println("Veterinarios encontrados: " + veterinarios.size());

            // Construir lista de DTOs
            List<VeterinarioDTO> veterinariosDTO = new ArrayList<>();
            for (Veterinario v : veterinarios) {
                String email = sesionRepository.findByUsuario_Id(v.getUsuario().getId())
                    .map(s -> s.getCorreo()).orElse(null);
                veterinariosDTO.add(new VeterinarioDTO(
                    v.getUsuario().getNombres(),
                    v.getUsuario().getApellidos(),
                    v.getEspecialidad(),
                    v.getUsuario().getCelular(),
                    email
                ));
            }
            
            // Depurar: Imprimir los primeros veterinarios
            for (int i = 0; i < Math.min(veterinarios.size(), 3); i++) {
                Veterinario v = veterinarios.get(i);
                System.out.println("Veterinario " + (i+1) + ": " + 
                                 v.getUsuario().getNombres() + " " + v.getUsuario().getApellidos() + 
                                 " - " + v.getEspecialidad());
            }
            
            // Agregar datos al modelo
            model.addAttribute("citasHoy", citasHoy);
            model.addAttribute("veterinariosDTO", veterinariosDTO);
            
            return "recepcionista/dashboard";
        } catch (Exception e) {
            System.err.println("Error in recepcionistaDashboard: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al cargar el dashboard: " + e.getMessage());
            return "redirect:/";
        }
    }
    
    // Dashboard de Administrador
    @GetMapping("/administrador/dashboard")
    public String administradorDashboard(HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        if (!authService.isLoggedIn(session)) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para acceder a esta página");
            return "redirect:/login";
        }
        AuthService.UserType userType = authService.getCurrentUserType(session);
        if (userType != AuthService.UserType.ADMINISTRADOR) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para acceder a esta página");
            return "redirect:/";
        }
        // Estadísticas
        long totalDuenos = duenoRepository.count();
        long totalMascotas = mascotaRepository.count();
        long totalVeterinarios = veterinarioRepository.count();
        long totalRecepcionistas = recepcionistaRepository.count();
        long totalEmpleadosClinica = empleadoClinicaRepository.count();
        long totalEmpleados = totalVeterinarios + totalRecepcionistas + totalEmpleadosClinica;
        long totalCitas = citaRepository.count();
        // Citas del día
        long citasHoy = citaRepository.findByFecha(LocalDate.now()).size();
        // Citas pendientes de pago
        long citasPendientesPago = citaRepository.findCitasSinPago().size();
        // Citas pagadas
        long citasPagadas = citaRepository.findCitasConPago().size();
        // Pagos
        var boletas = boletaPagoRepository.findAll();
        double totalIngresos = boletas.stream().mapToDouble(b -> b.getMonto_total() != null ? b.getMonto_total() : 0).sum();
        long totalPagos = boletas.size();
        long efectivo = boletas.stream().filter(b -> "Efectivo".equalsIgnoreCase(b.getMetodo_pago())).count();
        long pos = boletas.stream().filter(b -> "POS".equalsIgnoreCase(b.getMetodo_pago())).count();
        long yape = boletas.stream().filter(b -> "Yape".equalsIgnoreCase(b.getMetodo_pago())).count();
        long plin = boletas.stream().filter(b -> "Plin".equalsIgnoreCase(b.getMetodo_pago())).count();
        long transferencia = boletas.stream().filter(b -> "Transferencia".equalsIgnoreCase(b.getMetodo_pago())).count();
        long otros = boletas.stream().filter(b -> {
            String m = b.getMetodo_pago();
            return m != null && !m.equalsIgnoreCase("Efectivo") && !m.equalsIgnoreCase("POS") && !m.equalsIgnoreCase("Yape") && !m.equalsIgnoreCase("Plin") && !m.equalsIgnoreCase("Transferencia");
        }).count();
        // Pasar al modelo
        model.addAttribute("totalDuenos", totalDuenos);
        model.addAttribute("totalMascotas", totalMascotas);
        model.addAttribute("totalEmpleados", totalEmpleados);
        model.addAttribute("totalCitas", totalCitas);
        model.addAttribute("citasHoy", citasHoy);
        model.addAttribute("citasPendientesPago", citasPendientesPago);
        model.addAttribute("citasPagadas", citasPagadas);
        model.addAttribute("totalPagos", totalPagos);
        model.addAttribute("totalIngresos", totalIngresos);
        model.addAttribute("efectivo", efectivo);
        model.addAttribute("pos", pos);
        model.addAttribute("yape", yape);
        model.addAttribute("plin", plin);
        model.addAttribute("transferencia", transferencia);
        model.addAttribute("otros", otros);
        return "administrador/dashboard";
    }

    @GetMapping("/administrador/usuarios")
    public String redirectUsuarios() {
        return "redirect:/administrador/gestion-usuarios";
    }

    @GetMapping("/administrador/gestion-usuarios")
    public String adminGestionUsuarios() {
        return "administrador/gestion-usuarios";
    }

    @GetMapping("/recepcionista/veterinarios")
    public String recepcionistaVeterinarios(HttpSession session, RedirectAttributes redirectAttributes) {
        if (!authService.isLoggedIn(session)) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para acceder a esta página");
            return "redirect:/login";
        }
        AuthService.UserType userType = authService.getCurrentUserType(session);
        if (userType != AuthService.UserType.RECEPCIONISTA) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para acceder a esta página");
            return "redirect:/";
        }
        return "recepcionista/veterinarios";
    }

    // Página de Pacientes para Veterinario
    @GetMapping("/veterinario/pacientes")
    public String veterinarioPacientes(HttpSession session, RedirectAttributes redirectAttributes) {
        if (!authService.isLoggedIn(session)) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para acceder a esta página");
            return "redirect:/login";
        }
        AuthService.UserType userType = authService.getCurrentUserType(session);
        if (userType != AuthService.UserType.VETERINARIO) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para acceder a esta página");
            return "redirect:/";
        }
        return "veterinario/pacientes";
    }

    // Página de Horarios para Veterinario
    @GetMapping("/veterinario/horarios")
    public String veterinarioHorarios(HttpSession session, RedirectAttributes redirectAttributes) {
        if (!authService.isLoggedIn(session)) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para acceder a esta página");
            return "redirect:/login";
        }
        AuthService.UserType userType = authService.getCurrentUserType(session);
        if (userType != AuthService.UserType.VETERINARIO) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para acceder a esta página");
            return "redirect:/";
        }
        return "veterinario/horarios";
    }

    // Página de Historiales para Veterinario
    @GetMapping("/veterinario/historiales")
    public String veterinarioHistoriales(HttpSession session, RedirectAttributes redirectAttributes) {
        if (!authService.isLoggedIn(session)) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para acceder a esta página");
            return "redirect:/login";
        }
        AuthService.UserType userType = authService.getCurrentUserType(session);
        if (userType != AuthService.UserType.VETERINARIO) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para acceder a esta página");
            return "redirect:/";
        }
        return "veterinario/historiales";
    }
} 