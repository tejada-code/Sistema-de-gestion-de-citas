package com.utp.integradorspringboot.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.utp.integradorspringboot.models.Mascota;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {
    
    // Puedes agregar métodos personalizados si lo necesitas, por ejemplo:
    List<Mascota> findByDuenoId(Long duenoId);
    
    // Método para cargar mascotas con dueño y usuario en una sola consulta
    @Query("SELECT m FROM Mascota m JOIN FETCH m.dueno d JOIN FETCH d.usuario")
    List<Mascota> findAllWithDuenoAndUsuario();

    // Método estándar para obtener todas las mascotas sin JOIN FETCH
    List<Mascota> findAll();
}
