package com.example.Vaidmassas.service;

import com.example.Vaidmassas.dto.InsumoRequestDTO;
import com.example.Vaidmassas.model.Insumo;
import com.example.Vaidmassas.repository.InsumoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InsumoService {

    @Autowired
    private InsumoRepository repository;

    public Insumo salvar(InsumoRequestDTO dto) {
        Insumo novoInsumo = Insumo.builder()
                .nome(dto.nome())
                .quantidadeEmEstoque(dto.quantidadeEmEstoque())
                .unidadeDeMedida(dto.unidadeDeMedida())
                .build();

        return repository.save(novoInsumo);
    }

    public List<Insumo> listarTodos() {
        return repository.findAll();
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    public Insumo atualizar(Long id, InsumoRequestDTO dto) {
        Insumo insumoExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insumo não encontrado"));

        insumoExistente.setNome(dto.nome());
        insumoExistente.setQuantidadeEmEstoque(dto.quantidadeEmEstoque());
        insumoExistente.setUnidadeDeMedida(dto.unidadeDeMedida());

        return repository.save(insumoExistente);
    }
}