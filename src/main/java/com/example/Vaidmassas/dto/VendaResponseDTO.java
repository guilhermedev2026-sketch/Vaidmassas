package com.example.Vaidmassas.dto;

import java.time.LocalDateTime;

public record VendaResponseDTO(
        Long id,
        String nomeProduto,
        double valorVenda,
        LocalDateTime dataHora
) {}
