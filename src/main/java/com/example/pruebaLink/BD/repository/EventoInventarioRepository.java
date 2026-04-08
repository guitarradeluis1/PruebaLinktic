package com.example.pruebaLink.BD.repository;

import com.example.pruebaLink.BD.domain.EventoInventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventoInventarioRepository extends JpaRepository<EventoInventario, Long> {
    List<EventoInventario> findByInventarioIdOrderByFechaEventoDesc(Long inventarioId);

}
