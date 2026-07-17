package dev.llucascr.restaurant.dto;

import dev.llucascr.restaurant.domain.entity.CategoriaProduto;
import dev.llucascr.restaurant.domain.entity.Produto;

import java.math.BigDecimal;

public record ProdutoRequest(
        Long categoriaId,
        String nome,
        String descricao,
        BigDecimal preco,
        Boolean disponivel,
        Integer tempoPreparoMinutos
) {

    public Produto toEntity(CategoriaProduto categoria) {
        Produto produto = new Produto();
        preecher(produto, categoria);
        return produto;
    }

    public void preecher(Produto produto, CategoriaProduto categoria) {
        produto.setCategoria(categoria);
        produto.setNome(nome);
        produto.setDescricao(descricao);
        produto.setPreco(preco);
        produto.setDisponivel(disponivel != null ? disponivel : false);
        produto.setTempoPreparoMinutos(tempoPreparoMinutos);
    }

}
