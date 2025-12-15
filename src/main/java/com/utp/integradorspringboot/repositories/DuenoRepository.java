package com.utp.integradorspringboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.utp.integradorspringboot.models.Dueno;

@Repository
public interface DuenoRepository extends JpaRepository<Dueno, Long> {
    // Las operaciones CRUD básicas son proporcionadas automáticamente por JpaRepository
} 
