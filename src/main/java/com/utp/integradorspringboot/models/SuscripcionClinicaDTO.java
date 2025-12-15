package com.utp.integradorspringboot.models;

public class SuscripcionClinicaDTO {
    private Clinica clinica_veterinaria;
    private Usuario usuario;
    private Veterinario veterinario;
    private Sesion sesion;
    
    public SuscripcionClinicaDTO() {
        
    }

    public Clinica getClinica_veterinaria() {
        return clinica_veterinaria;
    }

    public void setClinica_veterinaria(Clinica clinica_veterinaria) {
        this.clinica_veterinaria = clinica_veterinaria;
    }

    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }

    public Sesion getSesion() {
        return sesion;
    }

    public void setSesion(Sesion sesion) {
        this.sesion = sesion;
    }

    @Override
    public String toString() {
        return "SuscripcionClinicaDTO{" + "clinica_veterinaria=" + clinica_veterinaria + ", usuario=" + usuario + ", veterinario=" + veterinario + ", sesion=" + sesion + '}';
    }

    
    
}
