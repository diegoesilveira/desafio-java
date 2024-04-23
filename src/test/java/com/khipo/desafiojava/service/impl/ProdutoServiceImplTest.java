package com.khipo.desafiojava.service.impl;

import com.khipo.desafiojava.controller.dto.produto.ProdutoDto;
import com.khipo.desafiojava.enums.CategoriaEnum;
import com.khipo.desafiojava.exception.DadoNaoEncontradoException;
import com.khipo.desafiojava.repository.PedidoRepository;
import com.khipo.desafiojava.repository.ProdutoRepository;
import com.khipo.desafiojava.repository.entity.ProdutoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProdutoServiceImplTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private PedidoRepository pedidoRepository;
    @InjectMocks
    private ProdutoServiceImpl produtoService;

    @BeforeEach
    void setUp() {
        new ProdutoServiceImpl(produtoRepository, pedidoRepository);
    }
    @Test
    void testListarProdutos() {

        List<ProdutoEntity> produtosEntity = Arrays.asList(
                new ProdutoEntity(1L, "Produto 1", BigDecimal.valueOf(10.0), CategoriaEnum.PRATO_PRINCIPAL),
                new ProdutoEntity(2L, "Produto 2",  BigDecimal.valueOf(20.0), CategoriaEnum.PRATO_PRINCIPAL)
        );
        Page<ProdutoEntity> produtosPageEntity = new PageImpl<>(produtosEntity);

        when(produtoRepository.findAll(any(Pageable.class))).thenReturn(produtosPageEntity);

        Page<ProdutoDto> resultado = produtoService.listarProdutos(Pageable.unpaged());
        assertEquals(produtosEntity.size(), resultado.getContent().size());
    }

    @Test
    void testCriarProduto() {
        ProdutoDto produtoDto = new ProdutoDto();
        produtoDto.setNome("Produto Teste");
        produtoDto.setPreco(BigDecimal.valueOf(10.0));
        produtoDto.setCategoriaId(3);

        ProdutoEntity produtoEntity = new ProdutoEntity();
        produtoEntity.setId(1L);
        produtoEntity.setNome(produtoDto.getNome());
        produtoEntity.setPreco(produtoDto.getPreco());
        produtoEntity.setCategoria(CategoriaEnum.PRATO_PRINCIPAL);

        when(produtoRepository.save(any(ProdutoEntity.class))).thenReturn(produtoEntity);

        ProdutoDto resultado = produtoService.criarProduto(produtoDto);

        assertEquals(produtoEntity.getId(), resultado.getId());
        assertEquals(produtoEntity.getNome(), resultado.getNome());
        assertEquals(produtoEntity.getPreco(), resultado.getPreco());
    }
    @Test
    void testAtualizarProduto() {
        Long idProduto = 1L;

        ProdutoDto produtoDto = new ProdutoDto();
        produtoDto.setNome("Produto Atualizado");
        produtoDto.setPreco(BigDecimal.valueOf(15.0));
        produtoDto.setCategoriaId(3);

        ProdutoEntity produtoEntity = new ProdutoEntity();
        produtoEntity.setId(idProduto);
        produtoEntity.setNome("Produto Existente");
        produtoEntity.setPreco(BigDecimal.valueOf(10.0));
        produtoEntity.setCategoria(CategoriaEnum.PRATO_PRINCIPAL);

        when(produtoRepository.findById(idProduto)).thenReturn(Optional.of(produtoEntity));

        when(produtoRepository.save(any(ProdutoEntity.class))).thenReturn(produtoEntity);

        ProdutoDto resultado = produtoService.atualizarProduto(idProduto, produtoDto);

        assertEquals(idProduto, resultado.getId());
        assertEquals(produtoDto.getNome(), resultado.getNome());
        assertEquals(produtoDto.getPreco(), resultado.getPreco());
    }
    @Test
    void testDeletarProduto() {
        Long idProduto = 1L;

        when(produtoRepository.findById(idProduto)).thenReturn(Optional.empty());
        assertThrows(DadoNaoEncontradoException.class, () -> produtoService.deletarProduto(idProduto));
        verify(produtoRepository, never()).deleteById(idProduto);
    }



}