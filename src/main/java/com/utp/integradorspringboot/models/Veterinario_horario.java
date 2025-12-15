package com.utp.integradorspringboot.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "veterinario_horario")
public class Veterinario_horario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_veterinario", nullable = false)
    private Veterinario veterinario;

    @ManyToOne
    @JoinColumn(name = "id_horario", nullable = false)
    private Horario_laboral horario;

    public Veterinario_horario() {}

    public Veterinario_horario(Long id, Veterinario veterinario, Horario_laboral horario) {
        this.id = id;
        this.veterinario = veterinario;
        this.horario = horario;
    }

    public Long getId() {
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
    public Horario_laboral getHorario() {
        return horario;
    }
    public void setHorario(Horario_laboral horario) {
        this.horario = horario;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Veterinario_horario other = (Veterinario_horario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Veterinario_horario{" +
                "id=" + id +
                ", veterinario=" + (veterinario != null ? veterinario.getId() : null) +
                ", horario=" + (horario != null ? horario.getId() : null) +
                '}';
    }
} 