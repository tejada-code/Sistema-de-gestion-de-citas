package com.utp.integradorspringboot.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utp.integradorspringboot.models.Dueno;
import com.utp.integradorspringboot.models.Mascota;
import com.utp.integradorspringboot.repositories.DuenoRepository;
import com.utp.integradorspringboot.repositories.MascotaRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class MascotaController {

    @Autowired
    MascotaRepository mascotaRepository;

    @Autowired
    DuenoRepository duenoRepository;

    // Endpoint de prueba para verificar si el controlador está funcionando
    @GetMapping("/Mascota/test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("¡El controlador de Mascota está funcionando!", HttpStatus.OK);
    }

    // Endpoint de prueba simple sin base de datos
    @GetMapping("/Mascota/simple")
    public ResponseEntity<Map<String, Object>> simpleTest() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Prueba simple exitosa");
            response.put("timestamp", new Date());
            response.put("estado", "OK");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error en la prueba simple: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Probar solo la conexión a la base de datos
    @GetMapping("/Mascota/db-test")
    public ResponseEntity<Map<String, Object>> dbTest() {
        try {
            long count = mascotaRepository.count();
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Conexión a la base de datos exitosa");
            response.put("cantidad_mascotas", count);
            response.put("estado", "OK");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error en la prueba de base de datos: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener todas las mascotas
    @GetMapping("/Mascota")
    public ResponseEntity<List<Mascota>> getAll() {
        try {
            System.out.println("Intentando obtener todas las mascotas...");
            
            // Usar el método personalizado que carga las relaciones en una sola consulta
            List<Mascota> lista = mascotaRepository.findAllWithDuenoAndUsuario();
            
            System.out.println("Se encontraron " + lista.size() + " mascotas (con join fetch)");

            for (Mascota m : lista) {
                System.out.println(m);
            }

            // Si no se encontraron mascotas con el método personalizado, usar el estándar
            if (lista.isEmpty()) {
                System.out.println("No se encontraron mascotas con join fetch, usando findAll estándar...");
                lista = mascotaRepository.findAll();
                System.out.println("Se encontraron " + lista.size() + " mascotas (findAll)");
            }

            if (lista.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(lista, HttpStatus.OK);

        } catch (org.springframework.dao.DataAccessException e) {
            System.err.println("Error de acceso a la base de datos en getAll mascotas: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            System.err.println("Error inesperado en getAll mascotas: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener mascota por ID
    @GetMapping("/Mascota/{id}")
    public ResponseEntity<Mascota> getById(@PathVariable("id") Long id) {
        Optional<Mascota> mascotaData = mascotaRepository.findById(id);

        if (mascotaData.isPresent()) {
            return new ResponseEntity<>(mascotaData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Obtener mascotas por ID de dueño
    @GetMapping("/Mascota/dueno/{duenoId}")
    public ResponseEntity<List<Mascota>> getByDuenoId(@PathVariable("duenoId") Long duenoId) {
        try {
            List<Mascota> mascotas = mascotaRepository.findByDuenoId(duenoId);

            if (mascotas.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(mascotas, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Crear nueva mascota
    @PostMapping("/Mascota")
    public ResponseEntity<Mascota> create(@RequestBody Mascota mascota) {
        try {
            // Validar que el usuario dueño exista
            if (mascota.getDueno() == null || mascota.getDueno().getId() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            Optional<Dueno> dueno = duenoRepository.findById(mascota.getDueno().getId());
            if (dueno.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            mascota.setDueno(dueno.get());
            
            // Validar campos requeridos
            if (mascota.getNombre() == null || mascota.getNombre().trim().isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            Mascota nuevaMascota = mascotaRepository.save(mascota);
            return new ResponseEntity<>(nuevaMascota, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar mascota
    @PutMapping("/Mascota/{id}")
    public ResponseEntity<Mascota> update(@PathVariable("id") Long id, @RequestBody Mascota mascota) {
        Optional<Mascota> mascotaData = mascotaRepository.findById(id);

        if (mascotaData.isPresent()) {
            Mascota _mascota = mascotaData.get();
            _mascota.setNombre(mascota.getNombre());
            _mascota.setRaza(mascota.getRaza());
            _mascota.setEdad(mascota.getEdad());

            // Opcional: actualizar usuario si lo necesitas
            if (mascota.getDueno() != null && mascota.getDueno().getId() != null) {
                Optional<Dueno> dueno = duenoRepository.findById(mascota.getDueno().getId());
                dueno.ifPresent(_mascota::setDueno);
            }

            return new ResponseEntity<>(mascotaRepository.save(_mascota), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar mascota
    @DeleteMapping("/Mascota/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
        try {
            mascotaRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
