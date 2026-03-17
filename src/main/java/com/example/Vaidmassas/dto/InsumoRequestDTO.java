package com.example.Vaidmassas.dto;

import com.example.Vaidmassas.model.Insumo.UnidadeMedida;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.NotNull;

public record InsumoRequestDTO(
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @PositiveOrZero(message = "A quantidade não pode ser negativa")
        int quantidadeEmEstoque,

        @NotNull(message = "A unidade de medida é obrigatória")
        UnidadeMedida unidadeDeMedida
) {
}
