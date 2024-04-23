package com.khipo.desafiojava.service.impl;


import com.khipo.desafiojava.controller.dto.pedido.PedidoDto;
import com.khipo.desafiojava.controller.dto.produto.ProdutoDto;
import com.khipo.desafiojava.enums.CategoriaEnum;
import com.khipo.desafiojava.exception.DadoNaoEncontradoException;
import com.khipo.desafiojava.repository.PedidoRepository;
import com.khipo.desafiojava.repository.ProdutoRepository;
import com.khipo.desafiojava.repository.entity.PedidoEntity;
import com.khipo.desafiojava.repository.entity.ProdutoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class PedidoServiceImplTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    @BeforeEach
    void setUp() {
        pedidoService = new PedidoServiceImpl(pedidoRepository, produtoRepository);
    }

    @Disabled
    void testCriarPedido() {
        Long idPedido = 1L;

        ProdutoEntity produtoEntity = new ProdutoEntity();
        produtoEntity.setId(1L);
        produtoEntity.setNome("Prato principal");
        produtoEntity.setPreco(BigDecimal.valueOf(10.0));
        produtoEntity.setCategoria(CategoriaEnum.PRATO_PRINCIPAL);

        PedidoEntity pedidoEntity = new PedidoEntity();
        pedidoEntity.setId(idPedido);
        pedidoEntity.setProdutos(List.of(produtoEntity));
        pedidoEntity.setValorTotal(BigDecimal.valueOf(10.00));

        ProdutoDto produtoDto = new ProdutoDto();
        produtoDto.setNome("Prato principal");
        produtoDto.setCategoriaId(3);
        produtoDto.setPreco(BigDecimal.valueOf(10.0));
        PedidoDto pedidoDto =  new PedidoDto();
        pedidoDto.setValorTotal(BigDecimal.valueOf(10.0));
        pedidoDto.setProdutos(List.of(produtoDto));

        when(pedidoRepository.save(pedidoEntity)).thenReturn(pedidoEntity);

        PedidoDto resultado = pedidoService.criarPedido(pedidoDto);

        assertNotNull(resultado);
        assertEquals(pedidoEntity.getId(), resultado.getId());
    }

    @Test
    void testListarPedidos() {
        Pageable pageable = Pageable.unpaged();
        Page<PedidoEntity> pedidoEntityPage = new PageImpl<>(Collections.emptyList());

        when(pedidoRepository.findAll(pageable)).thenReturn(pedidoEntityPage);

        Page<PedidoDto> resultado = pedidoService.listarPedidos(pageable);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void testAdicionarProdutoAoPedido() {
        Long idPedido = 1L;
        List<Long> idProdutos = Collections.singletonList(1L);

        ProdutoEntity produtoEntity = new ProdutoEntity();
        produtoEntity.setId(1L);
        produtoEntity.setNome("Prato principal");
        produtoEntity.setPreco(BigDecimal.valueOf(10.0));
        produtoEntity.setCategoria(CategoriaEnum.PRATO_PRINCIPAL);

        PedidoEntity pedidoEntity = new PedidoEntity();
        pedidoEntity.setId(idPedido);
        pedidoEntity.setProdutos(List.of(produtoEntity));
        pedidoEntity.setValorTotal(BigDecimal.valueOf(10.00));


        when(pedidoRepository.findById(idPedido)).thenReturn(Optional.of(pedidoEntity));
        when(produtoRepository.findAllById(idProdutos)).thenReturn(Collections.singletonList(produtoEntity));
        when(pedidoRepository.save(pedidoEntity)).thenReturn(pedidoEntity);
        PedidoDto resultado = pedidoService.adicionarProdutoAoPedido(idPedido, idProdutos);

        assertNotNull(resultado);
        assertFalse(resultado.getProdutos().isEmpty());
        assertEquals(BigDecimal.valueOf(20.0), resultado.getValorTotal());
    }

    @Test
    void testAdicionarProdutoAoPedido_PedidoNaoEncontrado() {
        Long idPedido = 1L;
        List<Long> idProdutos = Collections.singletonList(1L);

        when(pedidoRepository.findById(idPedido)).thenReturn(Optional.empty());

        assertThrows(DadoNaoEncontradoException.class, () -> pedidoService.adicionarProdutoAoPedido(idPedido, idProdutos));
    }

}