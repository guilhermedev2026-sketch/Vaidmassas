package com.example.Vaidmassas.service;

import com.example.Vaidmassas.dto.ItemReceitaDTO;
import com.example.Vaidmassas.dto.ProdutoRequestDTO;
import com.example.Vaidmassas.model.Insumo;
import com.example.Vaidmassas.model.ItemFichaTecnica;
import com.example.Vaidmassas.model.Produto;
import com.example.Vaidmassas.repository.InsumoRepository;
import com.example.Vaidmassas.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private InsumoRepository insumoRepository;

    @Transactional
    public Produto salvar(ProdutoRequestDTO dto) {
        // Converte os itens do DTO para a entidade ItemFichaTecnica
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

    // --- LÓGICA DE VENDA (BAIXA DE ESTOQUE) ---
    @Transactional
    public void realizarVenda(Long produtoId) {
        Produto produto = buscarPorId(produtoId);

        for (ItemFichaTecnica item : produto.getItensFicha()) {
            Insumo insumo = item.getInsumo();
            double quantidadeNecessaria = item.getQuantidadeUtilizada();

            if (insumo.getQuantidadeEmEstoque() < quantidadeNecessaria) {
                throw new RuntimeException("Estoque insuficiente para " + insumo.getNome() +
                        ". Necessário: " + quantidadeNecessaria + ", Disponível: " + insumo.getQuantidadeEmEstoque());
            }

            // Atualiza o estoque do insumo
            insumo.setQuantidadeEmEstoque((int) (insumo.getQuantidadeEmEstoque() - quantidadeNecessaria));
            insumoRepository.save(insumo);
        }
    }

    // --- MÉTODO AUXILIAR PARA CONSTRUIR A FICHA ---
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