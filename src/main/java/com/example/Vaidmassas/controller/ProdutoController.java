package com.example.Vaidmassas.controller;

import com.example.Vaidmassas.dto.ProdutoRequestDTO;
import com.example.Vaidmassas.model.Produto;
import com.example.Vaidmassas.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    // 1. Cadastrar Produto (com sua Ficha Técnica/Receita)
    @PostMapping
    public ResponseEntity<Produto> criar(@RequestBody @Valid ProdutoRequestDTO dados) {
        Produto novo = service.salvar(dados);
        return new ResponseEntity<>(novo, HttpStatus.CREATED);
    }

    // 2. Listar todos os produtos (já virão com os itens da ficha no JSON)
    @GetMapping
    public ResponseEntity<List<Produto>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // 3. Buscar um produto específico
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // 4. Endpoint de Venda (O "Coração" do Estoque)
    // Quando este endpoint é chamado, o estoque de Insumos diminui automaticamente
    @PostMapping("/{id}/vender")
    public ResponseEntity<String> vender(@PathVariable Long id) {
        try {
            service.realizarVenda(id);
            return ResponseEntity.ok("Venda processada: Ingredientes descontados do estoque com sucesso.");
        } catch (RuntimeException e) {
            // Caso falte algum ingrediente no estoque
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 5. Excluir Produto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
