package com.utp.integradorspringboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.utp.integradorspringboot.models.Cita;
import com.utp.integradorspringboot.models.Detalle_cita;

@Repository
public interface DetalleCitaRepository extends JpaRepository<Detalle_cita, Long> {
    // Las operaciones CRUD básicas son proporcionadas automáticamente por JpaRepository
    Detalle_cita findByCita(Cita cita);
} 