package com.utp.integradorspringboot.services;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utp.integradorspringboot.dto.PersonalDTO;
import com.utp.integradorspringboot.models.Clinica;
import com.utp.integradorspringboot.models.Recepcionista;
import com.utp.integradorspringboot.models.Rol;
import com.utp.integradorspringboot.models.Sesion;
import com.utp.integradorspringboot.models.Usuario;
import com.utp.integradorspringboot.models.Usuario_rol;
import com.utp.integradorspringboot.models.Veterinario;
import com.utp.integradorspringboot.repositories.ClinicaRepository;
import com.utp.integradorspringboot.repositories.RecepcionistaRepository;
import com.utp.integradorspringboot.repositories.RolRepository;
import com.utp.integradorspringboot.repositories.SesionRepository;
import com.utp.integradorspringboot.repositories.UsuarioRepository;
import com.utp.integradorspringboot.repositories.UsuarioRolRepository;
import com.utp.integradorspringboot.repositories.VeterinarioRepository;

/**
 * Servicio para gestionar personal (veterinarios y recepcionistas).
 */
@Service
public class GestionPersonalService {

    @Autowired
    private VeterinarioRepository veterinarioRepository;

    @Autowired
    private RecepcionistaRepository recepcionistaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClinicaRepository clinicaRepository;
    
    @Autowired
    private SesionRepository sesionRepository;
    
    @Autowired
    private RolRepository rolRepository;
    
    @Autowired
    private UsuarioRolRepository usuarioRolRepository;

    /**
     * Genera una contraseña aleatoria alfanumérica de 8-10 caracteres.
     */
    private String generarPassword() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        int longitud = random.nextInt(3) + 8; // 8-10 caracteres
        
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < longitud; i++) {
            password.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        
        return password.toString();
    }

    /**
     * Obtiene todo el personal (veterinarios y recepcionistas).
     */
    public List<PersonalDTO> getAllPersonal() {
        List<PersonalDTO> personalList = new ArrayList<>();

        // Obtener veterinarios
        List<Veterinario> veterinarios = veterinarioRepository.findAll();
        for (Veterinario veterinario : veterinarios) {
            Usuario usuario = veterinario.getUsuario();
            PersonalDTO dto = new PersonalDTO();
            dto.setId(veterinario.getId());
            dto.setTipo("Veterinario");
            dto.setDni(usuario.getDni());
            dto.setNombres(usuario.getNombres());
            dto.setApellidos(usuario.getApellidos());
            dto.setCelular(usuario.getCelular());
            dto.setFechaNacimiento(usuario.getFecha_nacimiento());
            dto.setFechaRegistro(usuario.getFecha_registro());
            dto.setNumeroColegioMedico(veterinario.getNumero_colegio_medico());
            dto.setEspecialidad(veterinario.getEspecialidad());
            
            // Obtener email de la sesión
            Optional<Sesion> sesionOpt = sesionRepository.findByUsuario_Id(usuario.getId());
            dto.setEmail(sesionOpt.map(Sesion::getCorreo).orElse(""));
            
            personalList.add(dto);
        }

        // Obtener recepcionistas
        List<Recepcionista> recepcionistas = recepcionistaRepository.findAll();
        for (Recepcionista recepcionista : recepcionistas) {
            Usuario usuario = recepcionista.getUsuario();
            Clinica clinica = recepcionista.getClinica();
            PersonalDTO dto = new PersonalDTO();
            dto.setId(recepcionista.getId());
            dto.setTipo("Recepcionista");
            dto.setDni(usuario.getDni());
            dto.setNombres(usuario.getNombres());
            dto.setApellidos(usuario.getApellidos());
            dto.setCelular(usuario.getCelular());
            dto.setFechaNacimiento(usuario.getFecha_nacimiento());
            dto.setFechaRegistro(usuario.getFecha_registro());
            dto.setClinica(clinica != null ? clinica.getNombre_clinica() : "");
            dto.setClinicaId(clinica != null ? clinica.getId() : null);
            
            // Obtener email de la sesión
            Optional<Sesion> sesionOpt = sesionRepository.findByUsuario_Id(usuario.getId());
            dto.setEmail(sesionOpt.map(Sesion::getCorreo).orElse(""));
            
            personalList.add(dto);
        }

        return personalList;
    }

    /**
     * Obtiene un personal por ID y tipo.
     */
    public PersonalDTO getPersonalById(Long id, String tipo) {
        if ("Veterinario".equals(tipo)) {
            Optional<Veterinario> veterinarioOpt = veterinarioRepository.findById(id);
            if (veterinarioOpt.isPresent()) {
                Veterinario veterinario = veterinarioOpt.get();
                Usuario usuario = veterinario.getUsuario();
                PersonalDTO dto = new PersonalDTO();
                dto.setId(veterinario.getId());
                dto.setTipo("Veterinario");
                dto.setDni(usuario.getDni());
                dto.setNombres(usuario.getNombres());
                dto.setApellidos(usuario.getApellidos());
                dto.setCelular(usuario.getCelular());
                dto.setFechaNacimiento(usuario.getFecha_nacimiento());
                dto.setFechaRegistro(usuario.getFecha_registro());
                dto.setNumeroColegioMedico(veterinario.getNumero_colegio_medico());
                dto.setEspecialidad(veterinario.getEspecialidad());
                
                // Obtener email de la sesión
                Optional<Sesion> sesionOpt = sesionRepository.findByUsuario_Id(usuario.getId());
                dto.setEmail(sesionOpt.map(Sesion::getCorreo).orElse(""));
                
                return dto;
            }
        } else if ("Recepcionista".equals(tipo)) {
            Optional<Recepcionista> recepcionistaOpt = recepcionistaRepository.findById(id);
            if (recepcionistaOpt.isPresent()) {
                Recepcionista recepcionista = recepcionistaOpt.get();
                Usuario usuario = recepcionista.getUsuario();
                Clinica clinica = recepcionista.getClinica();
                PersonalDTO dto = new PersonalDTO();
                dto.setId(recepcionista.getId());
                dto.setTipo("Recepcionista");
                dto.setDni(usuario.getDni());
                dto.setNombres(usuario.getNombres());
                dto.setApellidos(usuario.getApellidos());
                dto.setCelular(usuario.getCelular());
                dto.setFechaNacimiento(usuario.getFecha_nacimiento());
                dto.setFechaRegistro(usuario.getFecha_registro());
                dto.setClinica(clinica != null ? clinica.getNombre_clinica() : "");
                dto.setClinicaId(clinica != null ? clinica.getId() : null);
                
                // Obtener email de la sesión
                Optional<Sesion> sesionOpt = sesionRepository.findByUsuario_Id(usuario.getId());
                dto.setEmail(sesionOpt.map(Sesion::getCorreo).orElse(""));
                
                return dto;
            }
        }
        return null;
    }

    /**
     * Crea un nuevo personal.
     */
    public PersonalDTO createPersonal(PersonalDTO personalDTO) {
        // Validar que se proporcione un email
        if (personalDTO.getEmail() == null || personalDTO.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio para registrar personal");
        }
        
        // Verificar que el email no esté ya registrado
        Optional<Sesion> sesionExistente = sesionRepository.findByCorreo(personalDTO.getEmail().trim().toLowerCase());
        if (sesionExistente.isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado en el sistema");
        }
        
        // Crear usuario primero
        Usuario usuario = new Usuario();
        usuario.setNombres(personalDTO.getNombres());
        usuario.setApellidos(personalDTO.getApellidos());
        usuario.setDni(personalDTO.getDni());
        usuario.setCelular(personalDTO.getCelular());
        usuario.setFecha_nacimiento(personalDTO.getFechaNacimiento());
        usuario.setFecha_registro(LocalDateTime.now());
        
        Usuario savedUsuario = usuarioRepository.save(usuario);

        // Generar contraseña aleatoria
        String passwordGenerada = generarPassword();
        
        // Crear sesión para el usuario
        Sesion sesion = new Sesion();
        sesion.setCorreo(personalDTO.getEmail().trim().toLowerCase());
        sesion.setContrasena(passwordGenerada);
        sesion.setUsuario(savedUsuario);
        sesion.setFecha_creacion(LocalDateTime.now());
        sesionRepository.save(sesion);
        
        // Asignar rol según el tipo de personal
        String nombreRol = "";
        if ("Veterinario".equals(personalDTO.getTipo())) {
            nombreRol = "VETERINARIO";
        } else if ("Recepcionista".equals(personalDTO.getTipo())) {
            nombreRol = "RECEPCIONISTA";
        }
        
        if (!nombreRol.isEmpty()) {
            Optional<Rol> rolOpt = rolRepository.findByNombre(nombreRol);
            if (rolOpt.isPresent()) {
                Usuario_rol usuarioRol = new Usuario_rol();
                usuarioRol.setUsuario(savedUsuario);
                usuarioRol.setRol(rolOpt.get());
                usuarioRolRepository.save(usuarioRol);
            }
        }

        if ("Veterinario".equals(personalDTO.getTipo())) {
            Veterinario veterinario = new Veterinario();
            veterinario.setUsuario(savedUsuario);
            veterinario.setNumero_colegio_medico(personalDTO.getNumeroColegioMedico());
            veterinario.setEspecialidad(personalDTO.getEspecialidad());
            
            Veterinario savedVeterinario = veterinarioRepository.save(veterinario);
            personalDTO.setId(savedVeterinario.getId());
            personalDTO.setFechaRegistro(savedUsuario.getFecha_registro());
            
        } else if ("Recepcionista".equals(personalDTO.getTipo())) {
            Recepcionista recepcionista = new Recepcionista();
            recepcionista.setUsuario(savedUsuario);
            
            if (personalDTO.getClinicaId() != null) {
                Optional<Clinica> clinicaOpt = clinicaRepository.findById(personalDTO.getClinicaId());
                clinicaOpt.ifPresent(recepcionista::setClinica);
            }
            
            Recepcionista savedRecepcionista = recepcionistaRepository.save(recepcionista);
            personalDTO.setId(savedRecepcionista.getId());
            personalDTO.setFechaRegistro(savedUsuario.getFecha_registro());
        }
        
        // Establecer la contraseña generada en el DTO para devolverla al admin
        personalDTO.setPasswordGenerada(passwordGenerada);

        return personalDTO;
    }

    /**
     * Actualiza un personal existente.
     */
    public PersonalDTO updatePersonal(Long id, PersonalDTO personalDTO) {
        if ("Veterinario".equals(personalDTO.getTipo())) {
            Optional<Veterinario> veterinarioOpt = veterinarioRepository.findById(id);
            if (veterinarioOpt.isPresent()) {
                Veterinario veterinario = veterinarioOpt.get();
                Usuario usuario = veterinario.getUsuario();
                // Actualizar datos del usuario
                usuario.setNombres(personalDTO.getNombres());
                usuario.setApellidos(personalDTO.getApellidos());
                usuario.setDni(personalDTO.getDni());
                usuario.setCelular(personalDTO.getCelular());
                usuario.setFecha_nacimiento(personalDTO.getFechaNacimiento());
                // Actualizar datos del veterinario
                veterinario.setNumero_colegio_medico(personalDTO.getNumeroColegioMedico());
                veterinario.setEspecialidad(personalDTO.getEspecialidad());
                // Actualizar correo en Sesion si ha cambiado
                Optional<Sesion> sesionOpt = sesionRepository.findByUsuario_Id(usuario.getId());
                if (sesionOpt.isPresent() && personalDTO.getEmail() != null) {
                    Sesion sesion = sesionOpt.get();
                    if (!personalDTO.getEmail().equalsIgnoreCase(sesion.getCorreo())) {
                        sesion.setCorreo(personalDTO.getEmail().trim().toLowerCase());
                        sesionRepository.save(sesion);
                    }
                }
                usuarioRepository.save(usuario);
                veterinarioRepository.save(veterinario);
                personalDTO.setId(veterinario.getId());
                personalDTO.setFechaRegistro(usuario.getFecha_registro());
                return personalDTO;
            }
        } else if ("Recepcionista".equals(personalDTO.getTipo())) {
            Optional<Recepcionista> recepcionistaOpt = recepcionistaRepository.findById(id);
            if (recepcionistaOpt.isPresent()) {
                Recepcionista recepcionista = recepcionistaOpt.get();
                Usuario usuario = recepcionista.getUsuario();
                // Actualizar datos del usuario
                usuario.setNombres(personalDTO.getNombres());
                usuario.setApellidos(personalDTO.getApellidos());
                usuario.setDni(personalDTO.getDni());
                usuario.setCelular(personalDTO.getCelular());
                usuario.setFecha_nacimiento(personalDTO.getFechaNacimiento());
                // Actualizar clínica si se proporciona
                if (personalDTO.getClinicaId() != null) {
                    Optional<Clinica> clinicaOpt = clinicaRepository.findById(personalDTO.getClinicaId());
                    clinicaOpt.ifPresent(recepcionista::setClinica);
                }
                // Actualizar correo en Sesion si ha cambiado
                Optional<Sesion> sesionOpt = sesionRepository.findByUsuario_Id(usuario.getId());
                if (sesionOpt.isPresent() && personalDTO.getEmail() != null) {
                    Sesion sesion = sesionOpt.get();
                    if (!personalDTO.getEmail().equalsIgnoreCase(sesion.getCorreo())) {
                        sesion.setCorreo(personalDTO.getEmail().trim().toLowerCase());
                        sesionRepository.save(sesion);
                    }
                }
                usuarioRepository.save(usuario);
                recepcionistaRepository.save(recepcionista);
                personalDTO.setId(recepcionista.getId());
                personalDTO.setFechaRegistro(usuario.getFecha_registro());
                return personalDTO;
            }
        }
        return null;
    }

    /**
     * Elimina un personal por ID y tipo.
     */
    public boolean deletePersonal(Long id, String tipo) {
        try {
            if ("Veterinario".equals(tipo)) {
                Optional<Veterinario> veterinarioOpt = veterinarioRepository.findById(id);
                if (veterinarioOpt.isPresent()) {
                    Veterinario veterinario = veterinarioOpt.get();
                    Usuario usuario = veterinario.getUsuario();
                    veterinarioRepository.delete(veterinario);
                    usuarioRepository.delete(usuario);
                    return true;
                }
            } else if ("Recepcionista".equals(tipo)) {
                Optional<Recepcionista> recepcionistaOpt = recepcionistaRepository.findById(id);
                if (recepcionistaOpt.isPresent()) {
                    Recepcionista recepcionista = recepcionistaOpt.get();
                    Usuario usuario = recepcionista.getUsuario();
                    recepcionistaRepository.delete(recepcionista);
                    usuarioRepository.delete(usuario);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
} 