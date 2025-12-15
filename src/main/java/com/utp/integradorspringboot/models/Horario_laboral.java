package com.utp.integradorspringboot.models;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "horario_laboral")
public class Horario_laboral {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dia_semana")
    private String dia_semana;

    @Column(name = "hora_inicio")
    private LocalTime hora_inicio;

    @Column(name = "hora_fin")
    private LocalTime hora_fin;

    public Horario_laboral() {}

    public Horario_laboral(Long id, String dia_semana, LocalTime hora_inicio, LocalTime hora_fin) {
        this.id = id;
        this.dia_semana = dia_semana;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDia_semana() {
        return dia_semana;
    }
    public void setDia_semana(String dia_semana) {
        this.dia_semana = dia_semana;
    }
    public LocalTime getHora_inicio() {
        return hora_inicio;
    }
    public void setHora_inicio(LocalTime hora_inicio) {
        this.hora_inicio = hora_inicio;
    }
    public LocalTime getHora_fin() {
        return hora_fin;
    }
    public void setHora_fin(LocalTime hora_fin) {
        this.hora_fin = hora_fin;
    
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Horario_laboral other = (Horario_laboral) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Horario_laboral{" +
                "id=" + id +
                ", dia_semana='" + dia_semana + '\'' +
                ", hora_inicio=" + hora_inicio +
                ", hora_fin=" + hora_fin +
                '}';
    }
} 