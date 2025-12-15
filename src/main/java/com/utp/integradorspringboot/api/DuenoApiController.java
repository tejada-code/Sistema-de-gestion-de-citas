package com.utp.integradorspringboot.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.utp.integradorspringboot.models.Sesion;
import com.utp.integradorspringboot.repositories.DuenoRepository;
import com.utp.integradorspringboot.repositories.MascotaRepository;
import com.utp.integradorspringboot.repositories.NotificacionRepository;
import com.utp.integradorspringboot.repositories.SesionRepository;
import com.utp.integradorspringboot.repositories.UsuarioRepository;
import com.utp.integradorspringboot.repositories.UsuarioRolRepository;

@RestController
@RequestMapping("/api/duenos")
public class DuenoApiController {
    @Autowired
    private DuenoRepository duenoRepository;
    @Autowired
    private SesionRepository sesionRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioRolRepository usuarioRolRepository;
    @Autowired
    private MascotaRepository mascotaRepository;
    @Autowired
    private NotificacionRepository notificacionRepository;

    public static class DuenoDTO {
        public Long id;
        public String dni;
        public String nombres;
        public String apellidos;
        public String celular;
        public String email;
        public String fecha_registro; // Nuevo campo

        public DuenoDTO(Long id, String dni, String nombres, String apellidos, String celular, String email, String fecha_registro) {
            this.id = id;
            this.dni = dni;
            this.nombres = nombres;
            this.apellidos = apellidos;
            this.celular = celular;
            this.email = email;
            this.fecha_registro = fecha_registro;
        }
    }

    @GetMapping
    @ResponseBody
    public List<DuenoDTO> getAllDuenos() {
        return duenoRepository.findAll().stream().map(dueno -> {
            String email = null;
            if (dueno.getUsuario() != null) {
                // Buscar la sesión por ID de usuario
                Sesion sesion = sesionRepository.findByUsuario_Id(dueno.getUsuario().getId()).orElse(null);
                if (sesion != null) {
                    email = sesion.getCorreo();
                }
            }
            String fechaRegistro = dueno.getUsuario() != null && dueno.getUsuario().getFecha_registro() != null ? dueno.getUsuario().getFecha_registro().toString() : null;
            return new DuenoDTO(
                dueno.getId(),
                dueno.getUsuario() != null ? dueno.getUsuario().getDni() : null,
                dueno.getUsuario() != null ? dueno.getUsuario().getNombres() : null,
                dueno.getUsuario() != null ? dueno.getUsuario().getApellidos() : null,
                dueno.getUsuario() != null ? dueno.getUsuario().getCelular() : null,
                email,
                fechaRegistro
            );
        }).collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarDueno(@PathVariable Long id) {
        var duenoOpt = duenoRepository.findById(id);
        if (duenoOpt.isEmpty()) return ResponseEntity.notFound().build();

        var dueno = duenoOpt.get();
        var usuario = dueno.getUsuario();

        // Eliminar la sesión asociada al usuario
        sesionRepository.deleteByUsuarioId(usuario.getId());
        // Eliminar los roles asociados al usuario
        usuarioRolRepository.deleteByUsuarioId(usuario.getId());
        // Eliminar el dueño
        duenoRepository.delete(dueno);
        // Eliminar el usuario
        usuarioRepository.delete(usuario);

        return ResponseEntity.ok().build();
    }
} 