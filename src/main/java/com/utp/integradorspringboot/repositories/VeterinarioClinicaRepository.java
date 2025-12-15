package com.utp.integradorspringboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.utp.integradorspringboot.models.Veterinario_clinica;

@Repository
public interface VeterinarioClinicaRepository extends JpaRepository<Veterinario_clinica, Long> {
    // Las operaciones CRUD básicas son proporcionadas automáticamente por JpaRepository
} 