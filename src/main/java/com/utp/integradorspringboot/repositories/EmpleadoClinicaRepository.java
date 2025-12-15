package com.utp.integradorspringboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.utp.integradorspringboot.models.Empleado_clinica;

@Repository
public interface EmpleadoClinicaRepository extends JpaRepository<Empleado_clinica, Long> {
    // Las operaciones CRUD básicas son proporcionadas automáticamente por JpaRepository
    Empleado_clinica findByUsuario_IdAndTipoEmpleado(Long usuarioId, String tipoEmpleado);
} 