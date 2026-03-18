package com.example.Vaidmassas.dto;

import java.util.List;

public record ProdutoRequestDTO(
        String nome,
        double precoVenda,
        List<ItemReceitaDTO> itens // Nova lista com objetos detalhados
) {

}
a
