package com.khipo.desafiojava.service.impl;

import com.khipo.desafiojava.controller.dto.pedido.PedidoDto;
import com.khipo.desafiojava.controller.dto.produto.ProdutoDto;
import com.khipo.desafiojava.exception.DadoNaoEncontradoException;
import com.khipo.desafiojava.repository.PedidoRepository;
import com.khipo.desafiojava.repository.ProdutoRepository;
import com.khipo.desafiojava.repository.entity.PedidoEntity;
import com.khipo.desafiojava.repository.entity.ProdutoEntity;
import com.khipo.desafiojava.service.PedidoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.khipo.desafiojava.mapper.PedidoMapper.*;
import static com.khipo.desafiojava.util.MessagemException.PEDIDO_NAO_ENCONTRADO;

@Service
public class PedidoServiceImpl implements PedidoService {


    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoServiceImpl(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
    }

    /**
     * @return
     */
    @Override
    public PedidoDto criarPedido(PedidoDto pedidoDTO) {
        pedidoDTO.setValorTotal(somaValoresProdutosPedido(pedidoDTO.getProdutos()));
        PedidoEntity pedidoEntity = toEntity(pedidoDTO);
        PedidoEntity saved = pedidoRepository.save(pedidoEntity);
        return toDto(saved);
    }

    /**
     * @return
     */
    @Override
    public Page<PedidoDto> listarPedidos(Pageable pageable) {
        return toDtoPage(pedidoRepository.findAll(pageable));
    }

    /**
     * @param idPedido
     * @param idProdutos
     * @return
     */
    @Override
    public PedidoDto adicionarProdutoAoPedido(Long idPedido, List<Long> idProdutos) {
        PedidoEntity pedidoEntity = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new DadoNaoEncontradoException(PEDIDO_NAO_ENCONTRADO));

        List<ProdutoEntity> produtoEntityList = produtoRepository.findAllById(idProdutos);

        pedidoEntity.adicionarProduto(produtoEntityList);
        PedidoEntity pedido = pedidoRepository.save(pedidoEntity);
        return toDto(pedido);
    }

    private BigDecimal somaValoresProdutosPedido(List<ProdutoDto> produtos) {
        return produtos.stream()
                .map(ProdutoDto::getPreco)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


    }
}
