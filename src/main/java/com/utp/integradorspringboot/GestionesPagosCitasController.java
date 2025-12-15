package com.utp.integradorspringboot;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.utp.integradorspringboot.models.Boleta_pago;
import com.utp.integradorspringboot.models.Cita;
import com.utp.integradorspringboot.models.Detalle_cita;
import com.utp.integradorspringboot.repositories.BoletaPagoRepository;
import com.utp.integradorspringboot.repositories.DetalleCitaRepository;
import com.utp.integradorspringboot.repositories.GestionCitaRepository;

/**
 *
 * @author jcerv
 */
@Controller
public class GestionesPagosCitasController {
    

    @Autowired
    private GestionCitaRepository citaRepository;
    
    @Autowired
    private DetalleCitaRepository detalleCitaRepository;
    
    @Autowired
    private BoletaPagoRepository boletaPagoRepository;

    @RequestMapping("/GestionesPagosCitas")
    public String page(Model model) {
        // Obtener citas con detalles para mostrar en la tabla de pagos
        List<Cita> citas = citaRepository.findAll();
        model.addAttribute("citas", citas);
        return "/recepcionista/GestionPagosCitas";
    }
    
    @GetMapping("/api/citas-pendientes-pago")
    @ResponseBody
    public List<Cita> getCitasPendientesPago() {
        // Obtener citas que no tienen boleta de pago asociada
        return citaRepository.findCitasSinPago();
    }
    
    @GetMapping("/api/citas-pagadas")
    @ResponseBody
    public List<Cita> getCitasPagadas() {
        // Obtener citas que ya tienen boleta de pago
        return citaRepository.findCitasConPago();
    }
    
    @GetMapping("/api/boletas-pago")
    @ResponseBody
    public List<Boleta_pago> getBoletasPago() {
        // Obtener todas las boletas de pago
        return boletaPagoRepository.findAll();
    }
    
    @GetMapping("/api/mascotas")
    @ResponseBody
    public List<Object> getMascotas() {
        // Obtener todas las mascotas
        return citaRepository.findAll().stream()
            .map(cita -> cita.getMascota())
            .filter(mascota -> mascota != null)
            .distinct()
            .collect(java.util.stream.Collectors.toList());
    }
    

    
    @PostMapping("/api/registrar-pago")
    @ResponseBody
    public String registrarPago(@RequestParam Long citaId, 
                               @RequestParam String metodoPago,
                               @RequestParam Double monto) {
        try {
            // Buscar la cita
            Cita cita = citaRepository.findById(citaId).orElse(null);
            if (cita == null) {
                return "ERROR: Cita no encontrada";
            }
            
            // Buscar el detalle de la cita
            Detalle_cita detalleCita = detalleCitaRepository.findByCita(cita);
            if (detalleCita == null) {
                return "ERROR: Detalle de cita no encontrado";
            }
            
            // Verificar si ya existe una boleta de pago
            if (boletaPagoRepository.findByDetalleCita(detalleCita).isPresent()) {
                return "ERROR: Esta cita ya tiene un pago registrado";
            }
            
            // Crear la boleta de pago
            Boleta_pago boleta = new Boleta_pago();
            boleta.setDetalle_cita(detalleCita);
            boleta.setMonto_total(monto);
            boleta.setMetodo_pago(metodoPago);
            boleta.setFecha_emision(java.time.LocalDateTime.now());
            
            boletaPagoRepository.save(boleta);
            
            return "SUCCESS: Pago registrado correctamente";
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }
}
