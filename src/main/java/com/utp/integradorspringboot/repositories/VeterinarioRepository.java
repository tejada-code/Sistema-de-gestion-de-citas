package com.utp.integradorspringboot.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.utp.integradorspringboot.models.Veterinario;

@Repository
public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {
    // Las operaciones CRUD básicas son proporcionadas automáticamente por JpaRepository
    com.utp.integradorspringboot.models.Veterinario findByUsuario(com.utp.integradorspringboot.models.Usuario usuario);

    @Query("SELECT v FROM Veterinario v JOIN FETCH v.usuario")
    List<Veterinario> findAllWithUsuario();
} 
