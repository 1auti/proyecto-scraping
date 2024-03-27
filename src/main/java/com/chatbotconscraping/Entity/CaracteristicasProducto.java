package com.chatbotconscraping.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "caracteristicas_producto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaracteristicasProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "valor")
    private String valor;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

}
