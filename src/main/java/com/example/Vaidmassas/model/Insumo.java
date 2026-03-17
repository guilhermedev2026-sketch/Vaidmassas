package com.example.Vaidmassas.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Insumo{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private int quantidadeEmEstoque;

    @Enumerated(EnumType.STRING)
    private UnidadeMedida unidadeDeMedida;

    public enum UnidadeMedida{
        KG, GRAMA, LITRO, UNIDADE, MILILITRO
    }
}
