package com.utp.integradorspringboot.models;

public class VeterinarioDTO {
    private String nombres;
    private String apellidos;
    private String especialidad;
    private String celular;
    private String email;

    public VeterinarioDTO(String nombres, String apellidos, String especialidad, String celular, String email) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.especialidad = especialidad;
        this.celular = celular;
        this.email = email;
    }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
} 