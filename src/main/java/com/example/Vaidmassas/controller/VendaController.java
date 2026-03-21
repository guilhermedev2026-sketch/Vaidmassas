package com.example.Vaidmassas.controller;

import com.example.Vaidmassas.dto.VendaResponseDTO;
import com.example.Vaidmassas.model.Venda;
import com.example.Vaidmassas.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vendas")
@CrossOrigin(origins = "*")
public class VendaController {

    @Autowired
    private VendaRepository repository;

    @GetMapping
    public List<VendaResponseDTO> listarHistorico() {
        List<Venda> vendas = repository.findAll(Sort.by(Sort.Direction.DESC, "dataHora"));

        return vendas.stream()
                .map(v -> new VendaResponseDTO(v.getId(), v.getNomeProduto(), v.getValorVenda(), v.getDataHora()))
                .collect(Collectors.toList());
    }
}