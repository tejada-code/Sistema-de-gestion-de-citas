package com.utp.integradorspringboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.utp.integradorspringboot.models.Recepcionista;

@Repository
public interface RecepcionistaRepository extends JpaRepository<Recepcionista, Long> {
    // Las operaciones CRUD básicas son proporcionadas automáticamente por JpaRepository
} 