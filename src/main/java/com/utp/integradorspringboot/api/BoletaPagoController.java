package com.utp.integradorspringboot.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.utp.integradorspringboot.models.Boleta_pago;
import com.utp.integradorspringboot.models.Cita;
import com.utp.integradorspringboot.models.Detalle_cita;
import com.utp.integradorspringboot.repositories.BoletaPagoRepository;
import com.utp.integradorspringboot.repositories.DetalleCitaRepository;
import com.utp.integradorspringboot.repositories.GestionCitaRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/pagos")
public class BoletaPagoController {

    @Autowired
    private BoletaPagoRepository boletaPagoRepository;

    @Autowired
    private GestionCitaRepository citaRepository;

    @Autowired
    private DetalleCitaRepository detalleCitaRepository;

    // Obtener todas las boletas de pago
    @GetMapping("/boletas")
    public ResponseEntity<List<Boleta_pago>> getAllBoletas() {
        try {
            List<Boleta_pago> boletas = boletaPagoRepository.findAll();
            return new ResponseEntity<>(boletas, HttpStatus.OK);
        } catch (Exception e) {
            // Retornar lista vacía en vez de error por ahora
            System.err.println("Error al obtener boletas: " + e.getMessage());
            return new ResponseEntity<>(List.of(), HttpStatus.OK);
        }
    }

    // Obtener boleta por ID
    @GetMapping("/boletas/{id}")
    public ResponseEntity<Boleta_pago> getBoletaById(@PathVariable Long id) {
        Optional<Boleta_pago> boleta = boletaPagoRepository.findById(id);
        if (boleta.isPresent()) {
            return new ResponseEntity<>(boleta.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Obtener boleta por ID de cita
    @GetMapping("/boletas/cita/{citaId}")
    public ResponseEntity<Boleta_pago> getBoletaByCitaId(@PathVariable Long citaId) {
        try {
            Cita cita = citaRepository.findById(citaId).orElse(null);
            if (cita == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Detalle_cita detalleCita = detalleCitaRepository.findByCita(cita);
            if (detalleCita == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Optional<Boleta_pago> boleta = boletaPagoRepository.findByDetalleCita(detalleCita);
            if (boleta.isPresent()) {
                return new ResponseEntity<>(boleta.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Registrar nuevo pago
    @PostMapping("/registrar")
    public ResponseEntity<String> registrarPago(@RequestParam Long citaId,
                                               @RequestParam String metodoPago,
                                               @RequestParam Double monto) {
        try {
            // Buscar la cita correspondiente
            Cita cita = citaRepository.findById(citaId).orElse(null);
            if (cita == null) {
                return new ResponseEntity<>("ERROR: Cita no encontrada", HttpStatus.BAD_REQUEST);
            }

            // Buscar el detalle de la cita
            Detalle_cita detalleCita = detalleCitaRepository.findByCita(cita);
            if (detalleCita == null) {
                return new ResponseEntity<>("ERROR: Detalle de cita no encontrado", HttpStatus.BAD_REQUEST);
            }

            // Verificar si ya existe una boleta de pago para esta cita
            if (boletaPagoRepository.findByDetalleCita(detalleCita).isPresent()) {
                return new ResponseEntity<>("ERROR: Esta cita ya tiene un pago registrado", HttpStatus.CONFLICT);
            }

            // Crear la boleta de pago y asociarla a la cita
            Boleta_pago boleta = new Boleta_pago();
            boleta.setDetalle_cita(detalleCita);
            boleta.setMonto_total(monto);
            boleta.setMetodo_pago(metodoPago);
            boleta.setFecha_emision(LocalDateTime.now());

            boletaPagoRepository.save(boleta);

            // Actualizar el estado de la cita a "Completada"
            cita.setEstado("Completada");
            citaRepository.save(cita);

            // Actualizar el estado del detalle de la cita
            detalleCita.setEstado("Completada");
            detalleCitaRepository.save(detalleCita);

            return new ResponseEntity<>("SUCCESS: Pago registrado correctamente", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("ERROR: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener estadísticas de pagos
    @GetMapping("/estadisticas")
    public ResponseEntity<Object> getEstadisticas() {
        try {
            List<Boleta_pago> todasLasBoletas = boletaPagoRepository.findAll();
            
            double totalRecaudado = todasLasBoletas.stream()
                .mapToDouble(Boleta_pago::getMonto_total)
                .sum();
            
            long totalPagos = todasLasBoletas.size();
            
            // Contar por método de pago
            long efectivo = todasLasBoletas.stream()
                .filter(b -> "Efectivo".equals(b.getMetodo_pago()))
                .count();
            
            long pos = todasLasBoletas.stream()
                .filter(b -> "POS".equals(b.getMetodo_pago()))
                .count();
            
            long yape = todasLasBoletas.stream()
                .filter(b -> "Yape".equals(b.getMetodo_pago()))
                .count();
            
            long plin = todasLasBoletas.stream()
                .filter(b -> "Plin".equals(b.getMetodo_pago()))
                .count();
            
            long transferencia = todasLasBoletas.stream()
                .filter(b -> "Transferencia".equals(b.getMetodo_pago()))
                .count();
            
            long otros = todasLasBoletas.stream()
                .filter(b -> !"Efectivo".equals(b.getMetodo_pago()) && 
                           !"POS".equals(b.getMetodo_pago()) && 
                           !"Yape".equals(b.getMetodo_pago()) && 
                           !"Plin".equals(b.getMetodo_pago()) && 
                           !"Transferencia".equals(b.getMetodo_pago()))
                .count();
            
            return new ResponseEntity<>(Map.of(
                "totalRecaudado", totalRecaudado,
                "totalPagos", totalPagos,
                "efectivo", efectivo,
                "pos", pos,
                "yape", yape,
                "plin", plin,
                "transferencia", transferencia,
                "otros", otros
            ), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/estadisticas/clinica/{clinicaId}")
    public ResponseEntity<Object> getEstadisticasPorClinica(@PathVariable Long clinicaId) {
        try {
            List<Boleta_pago> boletas = boletaPagoRepository.findByClinicaId(clinicaId);

            double totalRecaudado = boletas.stream()
                .mapToDouble(Boleta_pago::getMonto_total)
                .sum();

            long totalPagos = boletas.size();

            long efectivo = boletas.stream().filter(b -> "Efectivo".equalsIgnoreCase(b.getMetodo_pago())).count();
            long pos = boletas.stream().filter(b -> "POS".equalsIgnoreCase(b.getMetodo_pago())).count();
            long yape = boletas.stream().filter(b -> "Yape".equalsIgnoreCase(b.getMetodo_pago())).count();
            long plin = boletas.stream().filter(b -> "Plin".equalsIgnoreCase(b.getMetodo_pago())).count();
            long transferencia = boletas.stream().filter(b -> "Transferencia".equalsIgnoreCase(b.getMetodo_pago())).count();
            long otros = boletas.stream().filter(b -> {
                String m = b.getMetodo_pago();
                return m != null && !m.equalsIgnoreCase("Efectivo") && !m.equalsIgnoreCase("POS") && !m.equalsIgnoreCase("Yape") && !m.equalsIgnoreCase("Plin") && !m.equalsIgnoreCase("Transferencia");
            }).count();

            return new ResponseEntity<>(Map.of(
                "totalRecaudado", totalRecaudado,
                "totalPagos", totalPagos,
                "efectivo", efectivo,
                "pos", pos,
                "yape", yape,
                "plin", plin,
                "transferencia", transferencia,
                "otros", otros
            ), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/boletas/clinica/{clinicaId}")
    public ResponseEntity<List<Boleta_pago>> getBoletasByClinica(@PathVariable Long clinicaId) {
        try {
            List<Boleta_pago> boletas = boletaPagoRepository.findByClinicaId(clinicaId);
            return new ResponseEntity<>(boletas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(List.of(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/administrador/gestion-pagos")
    public String mostrarGestionPagos() {
        return "administrador/gestion-pagos";
    }
} 