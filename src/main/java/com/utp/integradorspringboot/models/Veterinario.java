package com.utp.integradorspringboot.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "veterinario")
public class Veterinario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "numero_colegio_medico", unique = true)
    private String numero_colegio_medico;

    @Column(name = "especialidad")
    private String especialidad;

    // Constructores
    public Veterinario() {
    }

    public Veterinario(Long id, Usuario usuario, String numero_colegio_medico, String especialidad) {
        this.id = id;
        this.usuario = usuario;
        this.numero_colegio_medico = numero_colegio_medico;
        this.especialidad = especialidad;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNumero_colegio_medico() {
        return numero_colegio_medico;
    }

    public void setNumero_colegio_medico(String numero_colegio_medico) {
        this.numero_colegio_medico = numero_colegio_medico;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }


    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Veterinario)) {
            return false;
        }
        Veterinario other = (Veterinario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Veterinario{" + "id=" + id + ", usuario=" + usuario + ", numero_colegio_medico=" + numero_colegio_medico + ", especialidad=" + especialidad +'}';
    }
}
