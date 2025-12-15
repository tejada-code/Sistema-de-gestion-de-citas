package com.utp.integradorspringboot.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
//@Table(name = "veterinario_clinica", uniqueConstraints = @UniqueConstraint(columnNames = {"id_veterinario", "id_clinica"}))
@Table(name = "veterinario_clinica")
public class Veterinario_clinica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_veterinario", nullable = false)
    private Veterinario veterinario;

    @ManyToOne
    @JoinColumn(name = "id_clinica", nullable = false)
    private Clinica clinica;

    public Veterinario_clinica() {}

    public Veterinario_clinica(Long id, Veterinario veterinario, Clinica clinica) {
        this.id = id;
        this.veterinario = veterinario;
        this.clinica = clinica;
    }

    public Long getId(){ 
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Veterinario getVeterinario() {
        return veterinario; 
    }
    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario; 
    }
    public Clinica getClinica() {
        return clinica;
    }
    public void setClinica(Clinica clinica) {
        this.clinica = clinica;
    }
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Veterinario_clinica other = (Veterinario_clinica) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Veterinario_clinica{" +
                "id=" + id +
                ", veterinario=" + (veterinario != null ? veterinario.getId() : null) +
                ", clinica=" + (clinica != null ? clinica.getId() : null) +
                '}';
    }
}
