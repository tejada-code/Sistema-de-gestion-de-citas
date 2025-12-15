package com.utp.integradorspringboot.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "clinica_veterinaria")
public class Clinica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_clinica")
    private String nombre_clinica;

    @Column(name = "ruc", unique = true)
    private String ruc;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "link_web")
    private String link_web;

    @Column(name = "plan_suscripcion")
    private String plan_suscripcion;

    @Column(name = "pasarela_pago")
    private String pasarela_pago;

    // Constructores
    public Clinica() {
    }

    public Clinica(Long id, String nombre_clinica, String ruc, String direccion, String telefono, String link_web, String plan_suscripcion, String pasarela_pago) {
        this.id = id;
        this.nombre_clinica = nombre_clinica;
        this.ruc = ruc;
        this.direccion = direccion;
        this.telefono = telefono;
        this.link_web = link_web;
        this.plan_suscripcion = plan_suscripcion;
        this.pasarela_pago = pasarela_pago;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre_clinica() {
        return nombre_clinica;
    }

    public void setNombre_clinica(String nombre_clinica) {
        this.nombre_clinica = nombre_clinica;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getLink_web() {
        return link_web;
    }

    public void setLink_web(String link_web) {
        this.link_web = link_web;
    }

    public String getPlan_suscripcion() {
        return plan_suscripcion;
    }

    public void setPlan_suscripcion(String plan_suscripcion) {
        this.plan_suscripcion = plan_suscripcion;
    }

    public String getPasarela_pago() {
        return pasarela_pago;
    }

    public void setPasarela_pago(String pasarela_pago) {
        this.pasarela_pago = pasarela_pago;
    }


    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Clinica)) {
            return false;
        }
        Clinica other = (Clinica) object;
        if((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "Clinica{" + "id=" + id + ", nombre_clinica=" + nombre_clinica + ", ruc=" + ruc + ", direccion=" + direccion + ", telefono=" + telefono + ", link_web=" + link_web + ", plan_suscripcion=" + plan_suscripcion + ", pasarela_pago=" + pasarela_pago +'}';
    }
}
