package com.example.Vaidmassas.controller;

import com.example.Vaidmassas.dto.InsumoRequestDTO;
import com.example.Vaidmassas.model.Insumo;
import com.example.Vaidmassas.service.InsumoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/insumos")
public class InsumoController {

    @Autowired
    private InsumoService service;

    @PostMapping
    public ResponseEntity<Insumo> criar(@RequestBody @Valid InsumoRequestDTO dados) {
        Insumo novoInsumo = service.salvar(dados);
        return new ResponseEntity<>(novoInsumo, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Insumo>> listar() {
        List<Insumo> lista = service.listarTodos();
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Insumo> atualizar(@PathVariable Long id, @RequestBody @Valid InsumoRequestDTO dados) {
        return ResponseEntity.ok(service.atualizar(id, dados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
