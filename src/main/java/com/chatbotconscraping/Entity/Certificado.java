package com.chatbotconscraping.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Certificados")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Certificado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "img")
    private String img;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;


}
