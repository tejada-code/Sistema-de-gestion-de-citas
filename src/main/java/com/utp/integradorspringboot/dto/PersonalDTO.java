package com.utp.integradorspringboot.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO que representa la información combinada de un Usuario, Veterinario o Recepcionista.
 * Se utiliza para transferir datos de personal en el sistema de gestión de clínicas veterinarias.
 * Permite manejar tanto veterinarios como recepcionistas en una sola estructura.
 */
public class PersonalDTO {
    
    private Long id;
    /**
     * Tipo de personal: puede ser "Veterinario" o "Recepcionista".
     */
    private String tipo;
    
    // Datos generales del usuario
    private String dni;
    private String nombres;
    private String apellidos;
    private String celular;
    private LocalDate fechaNacimiento;
    private LocalDateTime fechaRegistro;
    
    // Datos de acceso al sistema
    private String email;
    private String passwordGenerada;
    
    // Datos específicos de veterinario
    private String numeroColegioMedico;
    private String especialidad;
    
    // Datos específicos de recepcionista
    private String clinica;
    private Long clinicaId;
    
    // Datos comunes a ambos tipos de personal
    private String horarioLaboral;
    
    /**
     * Constructor por defecto.
     */
    public PersonalDTO() {
    }
    
    /**
     * Constructor completo para inicializar todos los campos del DTO.
     */
    public PersonalDTO(Long id, String tipo, String dni, String nombres, String apellidos, 
                      String celular, LocalDate fechaNacimiento, LocalDateTime fechaRegistro,
                      String email, String passwordGenerada, String numeroColegioMedico, 
                      String especialidad, String clinica, Long clinicaId, String horarioLaboral) {
        this.id = id;
        this.tipo = tipo;
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.celular = celular;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaRegistro = fechaRegistro;
        this.email = email;
        this.passwordGenerada = passwordGenerada;
        this.numeroColegioMedico = numeroColegioMedico;
        this.especialidad = especialidad;
        this.clinica = clinica;
        this.clinicaId = clinicaId;
        this.horarioLaboral = horarioLaboral;
    }
    
    // Métodos getter y setter para cada campo
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String getDni() {
        return dni;
    }
    
    public void setDni(String dni) {
        this.dni = dni;
    }
    
    public String getNombres() {
        return nombres;
    }
    
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
    
    public String getApellidos() {
        return apellidos;
    }
    
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    
    public String getCelular() {
        return celular;
    }
    
    public void setCelular(String celular) {
        this.celular = celular;
    }
    
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    
    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
    
    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPasswordGenerada() {
        return passwordGenerada;
    }
    
    public void setPasswordGenerada(String passwordGenerada) {
        this.passwordGenerada = passwordGenerada;
    }
    
    public String getNumeroColegioMedico() {
        return numeroColegioMedico;
    }
    
    public void setNumeroColegioMedico(String numeroColegioMedico) {
        this.numeroColegioMedico = numeroColegioMedico;
    }
    
    public String getEspecialidad() {
        return especialidad;
    }
    
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
    
    public String getClinica() {
        return clinica;
    }
    
    public void setClinica(String clinica) {
        this.clinica = clinica;
    }
    
    public Long getClinicaId() {
        return clinicaId;
    }
    
    public void setClinicaId(Long clinicaId) {
        this.clinicaId = clinicaId;
    }
    
    public String getHorarioLaboral() {
        return horarioLaboral;
    }
    
    public void setHorarioLaboral(String horarioLaboral) {
        this.horarioLaboral = horarioLaboral;
    }
    
    @Override
    public String toString() {
        return "PersonalDTO{" +
                "id=" + id +
                ", tipo='" + tipo + '\'' +
                ", dni='" + dni + '\'' +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", celular='" + celular + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", fechaRegistro=" + fechaRegistro +
                ", email='" + email + '\'' +
                ", passwordGenerada='" + passwordGenerada + '\'' +
                ", numeroColegioMedico='" + numeroColegioMedico + '\'' +
                ", especialidad='" + especialidad + '\'' +
                ", clinica='" + clinica + '\'' +
                ", clinicaId=" + clinicaId +
                ", horarioLaboral='" + horarioLaboral + '\'' +
                '}';
    }
} 