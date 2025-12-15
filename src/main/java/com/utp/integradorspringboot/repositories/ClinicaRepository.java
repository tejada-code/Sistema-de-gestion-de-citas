package com.utp.integradorspringboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.utp.integradorspringboot.models.Clinica;

@Repository
public interface ClinicaRepository extends JpaRepository<Clinica, Long> {
    // Las operaciones CRUD básicas son proporcionadas automáticamente por JpaRepository
} 