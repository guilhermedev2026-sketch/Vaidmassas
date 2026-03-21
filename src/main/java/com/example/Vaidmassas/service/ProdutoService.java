package com.example.Vaidmassas.service;

import com.example.Vaidmassas.dto.ItemReceitaDTO;
import com.example.Vaidmassas.dto.ProdutoRequestDTO;
import com.example.Vaidmassas.model.Insumo;
import com.example.Vaidmassas.model.ItemFichaTecnica;
import com.example.Vaidmassas.model.Produto;
import com.example.Vaidmassas.model.Venda;
import com.example.Vaidmassas.repository.InsumoRepository;
import com.example.Vaidmassas.repository.ProdutoRepository;
import com.example.Vaidmassas.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private InsumoRepository insumoRepository;

    @Autowired
    private VendaRepository vendaRepository;

    @Transactional
    public Produto salvar(ProdutoRequestDTO dto) {
        List<ItemFichaTecnica> fichaTecnica = construirFichaTecnica(dto.itens());

        Produto novoProduto = Produto.builder()
                .nome(dto.nome())
                .precoVenda(dto.precoVenda())
                .itensFicha(fichaTecnica)
                .build();

        return produtoRepository.save(novoProduto);
    }

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + id));
    }

    @Transactional
    public void deletar(Long id) {
        Produto produto = buscarPorId(id);
        produtoRepository.delete(produto);
    }
    @Transactional
    public void realizarVenda(Long produtoId) {
        Produto produto = buscarPorId(produtoId);

        for (ItemFichaTecnica item : produto.getItensFicha()) {
            Insumo insumo = item.getInsumo();
            double quantidadeNecessaria = item.getQuantidadeUtilizada();

            if (insumo.getQuantidadeEmEstoque() < quantidadeNecessaria) {
                throw new RuntimeException("Estoque insuficiente para " + insumo.getNome());
            }

            insumo.setQuantidadeEmEstoque((int) (insumo.getQuantidadeEmEstoque() - quantidadeNecessaria));
            insumoRepository.save(insumo);
        }

        Venda novaVenda = Venda.builder()
                .nomeProduto(produto.getNome())
                .valorVenda(produto.getPrecoVenda())
                .dataHora(LocalDateTime.now())
                .build();

        vendaRepository.save(novaVenda);
    }

    private List<ItemFichaTecnica> construirFichaTecnica(List<ItemReceitaDTO> itensDto) {
        return itensDto.stream().map(itemDto -> {
            Insumo insumo = insumoRepository.findById(itemDto.insumoId())
                    .orElseThrow(() -> new RuntimeException("Insumo ID " + itemDto.insumoId() + " não existe."));

            return ItemFichaTecnica.builder()
                    .insumo(insumo)
                    .quantidadeUtilizada(itemDto.quantidade())
                    .build();
        }).collect(Collectors.toList());
    }

}