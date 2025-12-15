package com.utp.integradorspringboot.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.utp.integradorspringboot.models.Notificacion;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    // Las operaciones CRUD básicas son proporcionadas automáticamente por JpaRepository
    @Query("SELECT n FROM Notificacion n WHERE n.cita.clinica.id = :clinicaId ORDER BY n.fecha_creacion DESC")
    List<Notificacion> findByClinicaId(@Param("clinicaId") Long clinicaId);
} 