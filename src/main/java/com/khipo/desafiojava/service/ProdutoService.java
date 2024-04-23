package com.khipo.desafiojava.service;

import com.khipo.desafiojava.controller.dto.produto.ProdutoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProdutoService {

    ProdutoDto criarProduto(ProdutoDto produtoDto);

    Page<ProdutoDto> listarProdutos(Pageable pageable);

    ProdutoDto atualizarProduto(Long id, ProdutoDto produtoDTO);

    void deletarProduto(Long id);
}
