package com.example.Vaidmassas.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemFichaTecnica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Vários itens de ficha podem referenciar o mesmo Insumo
    @JoinColumn(name = "insumo_id")
    private Insumo insumo;

    private double quantidadeUtilizada;
}
a