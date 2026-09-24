package com.cljtech.clinica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Garante que a raiz e rotas sem arquivo estático correspondente
 * devolvam o {@code index.html} do Angular (além do ResourceResolver).
 */
@Controller
public class SpaForwardController {

    @GetMapping("/")
    public String root() {
        return "forward:/index.html";
    }
}
