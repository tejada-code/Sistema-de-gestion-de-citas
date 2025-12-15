/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.integradorspringboot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author jcerv
 */
@Controller
public class GestionesCitasController {
    
    @RequestMapping("/GestionesCitas")
    public String page() {
        //model.addAttribute("atributo", "valor");
        return "/recepcionista/FormularioGestionCitas";
    }
}

