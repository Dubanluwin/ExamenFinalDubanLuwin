package com.example.ExamenFinalDubanLuwin.controllers;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ExamenFinalDubanLuwin.entities.UsuarioEntity;
import com.example.ExamenFinalDubanLuwin.services.UsuarioService;

@Controller
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    UsuarioService service;

    @GetMapping("/inicio")
    public String inicio(Model model) {
        logger.info("Pasando por GetMapping inicio");
        model.addAttribute("usuario", new UsuarioEntity());
        return "paginaPrincipal";
    }

    @PostMapping("/registrarUsuario")
    public String registrarUsuario(@ModelAttribute UsuarioEntity usuario, Model model) {
        LocalDate hoy = LocalDate.now();
        LocalDate fechaNacimiento = usuario.getFechaNacimiento();

        if (fechaNacimiento == null) {
            model.addAttribute("error", "La fecha de nacimiento es obligatoria.");
            return "usuarioRegistrado";
        }

        int edad = Period.between(fechaNacimiento, hoy).getYears();

        if (edad < 18) {
            model.addAttribute("error", "Debe ser mayor de 18 años para registrarse. Edad actual: " + edad);
            return "usuarioRegistrado";
        }

        try {
            service.registrarUsuario(usuario);
            model.addAttribute("usuario", usuario);
            return "usuarioRegistrado";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "usuarioRegistrado";
        }
    }

    @GetMapping("/usuarios")
    public String listarUsuarios(@RequestParam(required = false) String nombre,
            @RequestParam(required = false) String tipo,
            Model model) {
        List<UsuarioEntity> usuarios;

        if ((nombre == null || nombre.isEmpty()) && (tipo == null || tipo.isEmpty())) {
            usuarios = service.obtenerTodosUsuarios();
        } else {
            usuarios = service.buscarUsuarios(nombre, tipo);
        }

        model.addAttribute("usuarios", usuarios);
        return "listaUsuarios";
    }

}
