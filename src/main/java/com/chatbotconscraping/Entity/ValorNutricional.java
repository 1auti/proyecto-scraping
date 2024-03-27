package com.chatbotconscraping.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "valorNutricional")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ValorNutricional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "valorMedio")
    private String valorMedio;
    @Column(name = "porCada100gMl")
    private String porCada100gMl;
    @Column(name ="porCada1Porcion")
    private String porCada1Porcion;
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

}
