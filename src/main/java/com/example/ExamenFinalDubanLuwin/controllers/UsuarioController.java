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
        logger.info("Pasando por GetMapping /inicio");
        model.addAttribute("usuario", new UsuarioEntity());
        return "paginaPrincipal";
    }

    @PostMapping("/registrarUsuario")
    public String registrarUsuario(@ModelAttribute("usuario") UsuarioEntity usuario, Model model) {
        String error = service.validarUsuario(usuario);

        if (error != null) {
            logger.error("Error de validación para el usuario: {}, ID: {} - {}", usuario.getNombre(), usuario.getId(),
                    error);
            model.addAttribute("error", error);
            return "usuarioRegistrado";
        }

        try {
            service.registrarUsuario(usuario);
            model.addAttribute("usuario", usuario);
            logger.info("Usuario registrado correctamente.");
            return "usuarioRegistrado";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            logger.error("El usuario no se ha podido registrar.");
            return "usuarioRegistrado";
        }
    }

    @GetMapping("/usuarios")
    public String listarUsuarios(@RequestParam(required = false) String nombre,
            @RequestParam(required = false) String tipo,
            Model model) {

        logger.info("Pasando por GetMapping /usuarios");
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
