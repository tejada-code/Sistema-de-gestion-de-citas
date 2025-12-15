package com.utp.integradorspringboot.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "detalle_cita")
public class Detalle_cita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_cita", nullable = false)
    @JsonIgnore
    private Cita cita;

    @Column(name = "estado")
    private String estado = "pendiente";

    @ManyToOne
    @JoinColumn(name = "id_motivo_cita", nullable = false)
    private Motivo_cita motivo_cita;

    @Column(name = "diagnostico")
    private String diagnostico;

    @Column(name = "tratamiento")
    private String tratamiento;

    @Column(name = "receta")
    private String receta;

    @Column(name = "duracion_aproximada")
    private Integer duracion_aproximada;

    public Detalle_cita() {}

    public Detalle_cita(Long id, Cita cita, String estado, Motivo_cita motivo_cita, String diagnostico, String tratamiento, String receta, Integer duracion_aproximada) {
        this.id = id;
        this.cita = cita;
        this.estado = estado;
        this.motivo_cita = motivo_cita;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.receta = receta;
        this.duracion_aproximada = duracion_aproximada;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Cita getCita() { return cita; }
    public void setCita(Cita cita) { this.cita = cita; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Motivo_cita getMotivo_cita() { return motivo_cita; }
    public void setMotivo_cita(Motivo_cita motivo_cita) { this.motivo_cita = motivo_cita; }
    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    public String getTratamiento() { return tratamiento; }
    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }
    public String getReceta() { return receta; }
    public void setReceta(String receta) { this.receta = receta; }
    public Integer getDuracion_aproximada() { return duracion_aproximada; }
    public void setDuracion_aproximada(Integer duracion_aproximada) { this.duracion_aproximada = duracion_aproximada; }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Detalle_cita)) {
            return false;
        }
        Detalle_cita other = (Detalle_cita) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Detalle_cita{" + "id=" + id + ", cita=" + (cita != null ? cita.getId() : "null") + ", estado=" + estado + ", motivo_cita=" + (motivo_cita != null ? motivo_cita.getId() : "null") + ", diagnostico=" + diagnostico + ", tratamiento=" + tratamiento + ", receta=" + receta + ", duracion_aproximada=" + duracion_aproximada + '}';
    }
}
