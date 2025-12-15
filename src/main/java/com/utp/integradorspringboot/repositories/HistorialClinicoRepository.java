package com.utp.integradorspringboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.utp.integradorspringboot.models.Historial_clinico;

@Repository
public interface HistorialClinicoRepository extends JpaRepository<Historial_clinico, Long> {
    // Las operaciones CRUD básicas son proporcionadas automáticamente por JpaRepository
} 