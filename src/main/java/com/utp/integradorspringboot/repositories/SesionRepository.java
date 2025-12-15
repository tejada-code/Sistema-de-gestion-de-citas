// Repositorio para la entidad Sesion, proporciona operaciones CRUD y consultas personalizadas
package com.utp.integradorspringboot.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.utp.integradorspringboot.models.Sesion;

@Repository // Indica que esta interfaz es un repositorio de Spring
public interface SesionRepository extends JpaRepository<Sesion, Long> {
    // Buscar una sesión por correo (sensible a mayúsculas/minúsculas)
    Optional<Sesion> findByCorreo(String correo);
    // Buscar una sesión por correo (ignorando mayúsculas/minúsculas)
    Optional<Sesion> findByCorreoIgnoreCase(String correo);
    // Buscar una sesión por el ID del usuario
    Optional<Sesion> findByUsuario_Id(Long usuarioId);

    // Eliminar una sesión por el ID del usuario
    @Modifying
    @Transactional
    @Query("DELETE FROM Sesion s WHERE s.usuario.id = :usuarioId")
    void deleteByUsuarioId(@Param("usuarioId") Long usuarioId);
} 