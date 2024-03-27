package com.chatbotconscraping.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name ="descripcion")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Descripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "ingrediente")
    private String ingrediente;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

}
