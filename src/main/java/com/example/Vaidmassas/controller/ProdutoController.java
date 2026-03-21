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

    @PostMapping
    public ResponseEntity<Produto> criar(@RequestBody @Valid ProdutoRequestDTO dados) {
        Produto novo = service.salvar(dados);
        return new ResponseEntity<>(novo, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Produto>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
