package com.example.pruebaLink.BD.repository;

import com.example.pruebaLink.BD.domain.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    Optional<Inventario> findByProductoId(Long productoId);

    //@Modifying
    //@Query("UPDATE Inventario i SET i.cantidad = :cantidad WHERE i.producto.id = :productoId")
    //void updateCantidadByProductoId(@Param("productoId") Long productoId, @Param("cantidad") Integer cantidad);
}