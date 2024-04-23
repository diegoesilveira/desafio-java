package com.khipo.desafiojava.controller.v1.impl;

import com.khipo.desafiojava.controller.dto.produto.ProdutoDto;
import com.khipo.desafiojava.controller.v1.ProdutoController;
import com.khipo.desafiojava.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/produtos")
public class ProdutoControllerImpl implements ProdutoController {


    private final ProdutoService produtoService;

    public ProdutoControllerImpl(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }


    /**
     * @param produtoDTO
     * @return
     */
    @Override
    @PostMapping
    public ResponseEntity<ProdutoDto> criarProduto(@Valid @RequestBody ProdutoDto produtoDTO) {
        ProdutoDto produtoDto = produtoService.criarProduto(produtoDTO);
        return new ResponseEntity<>(produtoDto, HttpStatus.CREATED);
    }

    /**
     * @return
     */
    @Override
    @GetMapping
    public ResponseEntity<Page<ProdutoDto>> listarProdutos(Pageable pageable) {
        Page<ProdutoDto> produtoDtoPage = produtoService.listarProdutos(pageable);
        return new ResponseEntity<>(produtoDtoPage, HttpStatus.OK);
    }

    /**
     * @param id
     * @param produtoDTO
     * @return
     */
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDto> atualizarProduto(@PathVariable Long id, @Valid @RequestBody ProdutoDto produtoDTO) {
        ProdutoDto produtoDto = produtoService.atualizarProduto(id, produtoDTO);
        return new ResponseEntity<>(produtoDto, HttpStatus.OK);
    }

    /**
     * @param id
     * @return
     */
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        produtoService.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }
}
