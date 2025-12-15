package com.utp.integradorspringboot.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.utp.integradorspringboot.models.Veterinario_horario;

@Repository
public interface VeterinarioHorarioRepository extends JpaRepository<Veterinario_horario, Long> {
    // Las operaciones CRUD básicas son proporcionadas automáticamente por JpaRepository
    @Query("SELECT vh FROM Veterinario_horario vh WHERE vh.veterinario.id = :veterinarioId")
    List<Veterinario_horario> findByVeterinarioId(@Param("veterinarioId") Long veterinarioId);
} 