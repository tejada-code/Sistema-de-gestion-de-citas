package com.utp.integradorspringboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class ClinicasController {
    @Autowired
    @RequestMapping("/Clinicas")
    public String page() {
        
        return "SuscripcionClinica";
    }
    
    @GetMapping("/Clinicas/SuscripcionEnviada")
    public String suscripcionEnviada() {
        return "clinica/SuscripcionEnviada";
    }
}
