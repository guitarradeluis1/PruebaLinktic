package com.example.pruebaLink.BD.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "evento_inventario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inventario_id", nullable = false)
    private Long inventarioId;

    @Column(name = "fecha_evento", nullable = false)
    private LocalDateTime fechaEvento;

    @Column(name = "cantidad", nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidad;

    @Column(name = "tipo_evento", nullable = false)
    private String tipoEvento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventario_id",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_evento_inventario_inventario"))
    private Inventario inventario;
}
