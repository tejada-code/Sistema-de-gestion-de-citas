/* 
 * 
 *
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.integradorspringboot.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


/**
 *
 * @author UTP
 */
@Entity
@Table(name = "dueno")
public class Dueno {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @JsonIgnore
    @OneToMany(mappedBy = "dueno")
    private List<Mascota> mascotas;
    
    public Dueno(){
        
    }
    
    public Dueno(Long id, Usuario usuario){
        this.id = id;
        this.usuario = usuario;
    }
    
    //Setters y Getters

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

    public List<Mascota> getMascotas() { 
        return mascotas; 
    }
    public void setMascotas(List<Mascota> mascotas) {
        this.mascotas = mascotas; 
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Dueno)) {
            return false;
        }
        Dueno other = (Dueno) object;
        if ((this.getId() == null && other.id != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "Dueno{" + "id=" + id + ", usuario=" + (usuario != null ? usuario.getId() : "null") + '}';
    }
    
}