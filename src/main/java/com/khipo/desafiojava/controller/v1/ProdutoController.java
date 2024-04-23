package com.khipo.desafiojava.controller.v1;

import com.khipo.desafiojava.controller.dto.produto.ProdutoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ProdutoController {

    ResponseEntity<ProdutoDto> criarProduto( ProdutoDto produtoDTO);

    ResponseEntity<Page<ProdutoDto>> listarProdutos(Pageable pageable);

    ResponseEntity<ProdutoDto> atualizarProduto(Long id,ProdutoDto produtoDTO);

    ResponseEntity<Void> deletarProduto(Long id);
}
