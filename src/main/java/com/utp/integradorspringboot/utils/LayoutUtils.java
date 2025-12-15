package com.utp.integradorspringboot.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.utp.integradorspringboot.services.AuthService;

@Component
@SessionScope
public class LayoutUtils {
    
    public String getLayoutForUserType(AuthService.UserType userType) {
        switch (userType) {
            case DUENO:
                return "_layoutDueno";
            case VETERINARIO:
                return "_layoutVeterinario";
            case RECEPCIONISTA:
                return "_layoutRecepcionista";
            case ADMINISTRADOR:
                return "_layoutAdministrador";
            default:
                return "_layout";
        }
    }
} 