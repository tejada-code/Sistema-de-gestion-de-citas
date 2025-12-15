package com.utp.integradorspringboot.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.utp.integradorspringboot.models.Boleta_pago;
import com.utp.integradorspringboot.models.Detalle_cita;

@Repository
public interface BoletaPagoRepository extends JpaRepository<Boleta_pago, Long> {
    // Las operaciones CRUD básicas son proporcionadas automáticamente por JpaRepository
    
    // Buscar boleta por detalle de cita usando consulta personalizada
    @Query("SELECT b FROM Boleta_pago b WHERE b.detalle_cita = :detalleCita")
    Optional<Boleta_pago> findByDetalleCita(@Param("detalleCita") Detalle_cita detalleCita);

    @Query("SELECT b FROM Boleta_pago b " +
           "JOIN b.detalle_cita dc " +
           "JOIN dc.cita c " +
           "WHERE c.clinica.id = :clinicaId")
    List<Boleta_pago> findByClinicaId(@Param("clinicaId") Long clinicaId);
} 