/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.integradorspringboot.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 *
 * @author UTP
 */
@Entity
@Table(name = "empleado_clinica")
public class Empleado_clinica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_clinica", nullable = false)
    private Clinica clinica;

    @Column(name = "tipo_empleado")
    private String tipoEmpleado;

    public Empleado_clinica() {}

    public Empleado_clinica(Long id, Usuario usuario, Clinica clinica, String tipoEmpleado) {
        this.id = id;
        this.usuario = usuario;
        this.clinica = clinica;
        this.tipoEmpleado = tipoEmpleado;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Clinica getClinica() { return clinica; }
    public void setClinica(Clinica clinica) { this.clinica = clinica; }
    public String getTipoEmpleado() { return tipoEmpleado; }
    public void setTipoEmpleado(String tipoEmpleado) { this.tipoEmpleado = tipoEmpleado; }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Empleado_clinica)) {
            return false;
        }
        Empleado_clinica other = (Empleado_clinica) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "Cita{" + "id=" + getId() + ", ID de la Clinica=" + clinica.getId() + ", ID del Usuario=" + usuario.getId() + ", Tipo empleado="+tipoEmpleado+'}';
    }
}
