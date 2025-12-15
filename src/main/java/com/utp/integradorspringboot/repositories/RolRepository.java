package com.utp.integradorspringboot.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.utp.integradorspringboot.models.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    // Las operaciones CRUD básicas son proporcionadas automáticamente por JpaRepository
    Optional<Rol> findByNombre(String nombre);
} 