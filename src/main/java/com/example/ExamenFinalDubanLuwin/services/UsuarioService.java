package com.example.ExamenFinalDubanLuwin.services;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.ExamenFinalDubanLuwin.controllers.UsuarioController;
import com.example.ExamenFinalDubanLuwin.entities.UsuarioEntity;
import com.example.ExamenFinalDubanLuwin.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public void registrarUsuario(UsuarioEntity usuario) {
        if (!esMayorDeEdad(usuario.getFechaNacimiento())) {
            logger.error("Se ha propagado la excepcion en Usuario Service porque el usuario tiene menos de 18 años.");
            throw new IllegalArgumentException("Debes tener al menos 18 años para registrarte.");
        }
        repository.save(usuario);
    }

    private boolean esMayorDeEdad(LocalDate fechaNacimiento) {
        logger.info("Entrando la clase Usuario Service, en el método para controlar la edad esMayorEdad()");
        if (fechaNacimiento == null) {
            return false;
        }
        Period edad = Period.between(fechaNacimiento, LocalDate.now());
        return edad.getYears() > 18 || (edad.getYears() == 18 &&
                (edad.getMonths() > 0 || edad.getDays() >= 0));
    }

    public List<UsuarioEntity> obtenerTodosUsuarios() {
        logger.info("Entrando la clase Usuario Service, obteniendo todos los usuarios de la base de datos.");
        return repository.findAll();
    }

    public List<UsuarioEntity> buscarUsuarios(String nombre, String tipo) {
        logger.info("Entrando la clase Usuario Service, buscarUsuarios()");
        if ((nombre == null || nombre.isEmpty()) && (tipo == null || tipo.isEmpty())) {
            return repository.findAll();
        }
        if (nombre != null && !nombre.isEmpty() && tipo != null && !tipo.isEmpty()) {
            return repository.findByNombreContainingIgnoreCaseAndTipoIgnoreCase(nombre, tipo);
        }
        if (nombre != null && !nombre.isEmpty()) {
            return repository.findByNombreContainingIgnoreCase(nombre);
        }
        return repository.findByTipoIgnoreCase(tipo);
    }
}
