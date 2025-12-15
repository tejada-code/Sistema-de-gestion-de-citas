package com.utp.integradorspringboot.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.utp.integradorspringboot.models.Boleta_pago;
import com.utp.integradorspringboot.models.Cita;
import com.utp.integradorspringboot.models.Dueno;
import com.utp.integradorspringboot.models.Rol;
import com.utp.integradorspringboot.models.Sesion;
import com.utp.integradorspringboot.models.Usuario;
import com.utp.integradorspringboot.models.Usuario_rol;
import com.utp.integradorspringboot.repositories.BoletaPagoRepository;
import com.utp.integradorspringboot.repositories.DuenoRepository;
import com.utp.integradorspringboot.repositories.GestionCitaRepository;
import com.utp.integradorspringboot.repositories.RolRepository;
import com.utp.integradorspringboot.repositories.SesionRepository;
import com.utp.integradorspringboot.repositories.UsuarioRepository;
import com.utp.integradorspringboot.repositories.UsuarioRolRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class DuenoController {

    @Autowired
    private DuenoRepository duenoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioRolRepository usuarioRolRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private GestionCitaRepository gestionCitaRepository;
    @Autowired
    private BoletaPagoRepository boletaPagoRepository;
    @Autowired
    private SesionRepository sesionRepository;

    @GetMapping("/recepcionista/clientes")
    public String gestionarClientes(HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        // TODO: Add authentication/authorization check if needed
        List<Dueno> duenos = duenoRepository.findAll();
        model.addAttribute("duenos", duenos);
        return "recepcionista/gestionar_clientes";
    }

    @PostMapping("/recepcionista/clientes/enroll")
    @ResponseBody
    public Map<String, Object> enrollCliente(@RequestBody Map<String, String> data) {
        Map<String, Object> resp = new HashMap<>();
        try {
            String email = data.get("email");
            if (email == null || email.trim().isEmpty()) {
                resp.put("success", false);
                resp.put("error", "El email es obligatorio");
                return resp;
            }
            Usuario usuario = new Usuario();
            usuario.setNombres(data.get("nombres"));
            usuario.setApellidos(data.get("apellidos"));
            usuario.setDni(data.get("dni"));
            usuario.setCelular(data.get("celular"));
            usuario.setFecha_nacimiento(LocalDate.parse(data.get("fecha_nacimiento")));
            usuario.setFecha_registro(LocalDateTime.now());
            usuario = usuarioRepository.save(usuario);
            // Asignar rol DUENO
            Rol rolDueno = rolRepository.findByNombre("DUENO").orElseThrow();
            Usuario_rol ur = new Usuario_rol();
            ur.setUsuario(usuario);
            ur.setRol(rolDueno);
            usuarioRolRepository.save(ur);
            // Crear Dueno
            Dueno dueno = new Dueno();
            dueno.setUsuario(usuario);
            duenoRepository.save(dueno);
            // Crear Sesion asociada
            Sesion sesion = new Sesion();
            sesion.setUsuario(usuario);
            sesion.setCorreo(email);
            sesionRepository.save(sesion);
            resp.put("success", true);
            resp.put("dueno", dueno);
        } catch (Exception e) {
            resp.put("success", false);
            resp.put("error", e.getMessage());
        }
        return resp;
    }

    @GetMapping("/recepcionista/clientes/{duenoId}/citas")
    @ResponseBody
    public List<Map<String, Object>> getCitasByDueno(@PathVariable Long duenoId) {
        // Custom query needed in GestionCitaRepository
        List<Cita> citas = gestionCitaRepository.findAll().stream()
            .filter(c -> c.getDueno() != null && c.getDueno().getId().equals(duenoId))
            .collect(Collectors.toList());
        return citas.stream().map(cita -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", cita.getId());
            map.put("fecha", cita.getFecha());
            map.put("hora", cita.getHora());
            map.put("estado", cita.getEstado());
            String motivo = "";
            if (cita.getDetalleCita() != null && cita.getDetalleCita().getMotivo_cita() != null) {
                motivo = cita.getDetalleCita().getMotivo_cita().getNombre();
            }
            map.put("motivo", motivo);
            map.put("veterinario", cita.getVeterinario() != null && cita.getVeterinario().getUsuario() != null ? cita.getVeterinario().getUsuario().getNombres() + " " + cita.getVeterinario().getUsuario().getApellidos() : "");
            // Buscar boleta de pago asociada
            Boleta_pago boleta = null;
            if (cita.getDetalleCita() != null) {
                boleta = boletaPagoRepository.findByDetalleCita(cita.getDetalleCita()).orElse(null);
            }
            if (boleta != null) {
                map.put("pago_estado", boleta.getMetodo_pago());
                map.put("monto", boleta.getMonto_total());
            } else {
                map.put("pago_estado", "Pendiente");
                map.put("monto", "");
            }
            return map;
        }).collect(Collectors.toList());
    }

    @GetMapping("/api/duenos/{id}")
    @ResponseBody
    public Map<String, Object> obtenerDueno(@PathVariable Long id) {
        Map<String, Object> resp = new HashMap<>();
        try {
            Dueno dueno = duenoRepository.findById(id).orElse(null);
            if (dueno == null) {
                resp.put("success", false);
                resp.put("error", "Dueño no encontrado");
                return resp;
            }

            Usuario usuario = dueno.getUsuario();
            if (usuario == null) {
                resp.put("success", false);
                resp.put("error", "Usuario no encontrado");
                return resp;
            }

            // Devolver los datos del usuario
            Map<String, Object> usuarioData = new HashMap<>();
            usuarioData.put("id", usuario.getId());
            usuarioData.put("nombres", usuario.getNombres());
            usuarioData.put("apellidos", usuario.getApellidos());
            usuarioData.put("dni", usuario.getDni());
            usuarioData.put("celular", usuario.getCelular());
            usuarioData.put("fecha_nacimiento", usuario.getFecha_nacimiento() != null ? usuario.getFecha_nacimiento().toString() : null);
            usuarioData.put("fecha_registro", usuario.getFecha_registro());

            resp.put("success", true);
            resp.put("usuario", usuarioData);
            resp.put("dueno", dueno);
        } catch (Exception e) {
            resp.put("success", false);
            resp.put("error", e.getMessage());
        }
        return resp;
    }

    @PutMapping("/api/duenos/{id}")
    @ResponseBody
    public Map<String, Object> actualizarDueno(@PathVariable Long id, @RequestBody Map<String, Object> data) {
        Map<String, Object> resp = new HashMap<>();
        try {
            Dueno dueno = duenoRepository.findById(id).orElse(null);
            if (dueno == null) {
                resp.put("success", false);
                resp.put("error", "Dueño no encontrado");
                return resp;
            }

            Usuario usuario = dueno.getUsuario();
            if (usuario == null) {
                resp.put("success", false);
                resp.put("error", "Usuario no encontrado");
                return resp;
            }

            // Actualizar datos del usuario
            if (data.containsKey("nombres")) {
                usuario.setNombres((String) data.get("nombres"));
            }
            if (data.containsKey("apellidos")) {
                usuario.setApellidos((String) data.get("apellidos"));
            }
            if (data.containsKey("dni")) {
                usuario.setDni((String) data.get("dni"));
            }
            if (data.containsKey("celular")) {
                usuario.setCelular((String) data.get("celular"));
            }
            if (data.containsKey("fecha_nacimiento")) {
                String fechaStr = (String) data.get("fecha_nacimiento");
                if (fechaStr != null && !fechaStr.isEmpty()) {
                    usuario.setFecha_nacimiento(LocalDate.parse(fechaStr));
                }
            }

            // Guardar cambios
            usuarioRepository.save(usuario);
            duenoRepository.save(dueno);

            // Devolver los datos actualizados del usuario
            Map<String, Object> usuarioData = new HashMap<>();
            usuarioData.put("id", usuario.getId());
            usuarioData.put("nombres", usuario.getNombres());
            usuarioData.put("apellidos", usuario.getApellidos());
            usuarioData.put("dni", usuario.getDni());
            usuarioData.put("celular", usuario.getCelular());
            usuarioData.put("fecha_nacimiento", usuario.getFecha_nacimiento() != null ? usuario.getFecha_nacimiento().toString() : null);
            usuarioData.put("fecha_registro", usuario.getFecha_registro());

            resp.put("success", true);
            resp.put("message", "Dueño actualizado correctamente");
            resp.put("usuario", usuarioData);
            resp.put("dueno", dueno);
        } catch (Exception e) {
            resp.put("success", false);
            resp.put("error", e.getMessage());
        }
        return resp;
    }
} 