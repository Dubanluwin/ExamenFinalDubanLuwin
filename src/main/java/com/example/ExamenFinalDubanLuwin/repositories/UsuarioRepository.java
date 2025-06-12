package com.example.ExamenFinalDubanLuwin.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ExamenFinalDubanLuwin.entities.UsuarioEntity;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    List<UsuarioEntity> findByNombreContainingIgnoreCase(String nombre);

    List<UsuarioEntity> findByTipoIgnoreCase(String tipo);

    List<UsuarioEntity> findByNombreContainingIgnoreCaseAndTipoIgnoreCase(String nombre, String tipo);

}
