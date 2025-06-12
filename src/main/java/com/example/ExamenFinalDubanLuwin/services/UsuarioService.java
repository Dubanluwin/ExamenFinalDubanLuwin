package com.example.ExamenFinalDubanLuwin.services;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Service;

import com.example.ExamenFinalDubanLuwin.entities.UsuarioEntity;
import com.example.ExamenFinalDubanLuwin.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public void registrarUsuario(UsuarioEntity usuario) {
        if (!esMayorDeEdad(usuario.getFechaNacimiento())) {
            throw new IllegalArgumentException("Debes tener al menos 18 años para registrarte.");
        }
        repository.save(usuario);
    }

    private boolean esMayorDeEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            return false;
        }
        Period edad = Period.between(fechaNacimiento, LocalDate.now());
        return edad.getYears() > 18 || (edad.getYears() == 18 &&
                (edad.getMonths() > 0 || edad.getDays() >= 0));
    }
}
