package com.utp.integradorspringboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.utp.integradorspringboot.models.MascotaClinica;

public interface MascotaClinicaRepository extends JpaRepository<MascotaClinica, Long> {
} 