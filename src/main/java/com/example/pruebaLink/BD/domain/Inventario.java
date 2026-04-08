package com.example.pruebaLink.BD.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "inventario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Column(name = "cantidad", nullable = false)
    private BigDecimal cantidad = BigDecimal.ZERO;

    @Column(name = "fecha_actualizacion")
    private String fechaActualizacion;

    //@OneToMany(mappedBy = "inventario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //private List<EventoInventario> eventos;
}