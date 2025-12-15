package com.utp.integradorspringboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.utp.integradorspringboot.models.Motivo_cita;

@Repository
public interface MotivoCitaRepository extends JpaRepository<Motivo_cita, Long> {
    // Las operaciones CRUD básicas son proporcionadas automáticamente por JpaRepository
} 