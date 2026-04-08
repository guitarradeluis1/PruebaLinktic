package com.example.pruebaLink.BD.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "ordenservicio_productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenservicioProductos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    //@Column(name = "ordenservicio_id")
    //private Long ordenservicioId;

    @Column(name = "producto_id")
    private Long productoId;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "costo")
    private Double costo;
}
