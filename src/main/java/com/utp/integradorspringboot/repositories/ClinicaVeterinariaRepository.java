package com.utp.integradorspringboot.repositories;

import com.utp.integradorspringboot.models.Clinica;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClinicaVeterinariaRepository extends JpaRepository<Clinica, Long> {
}
