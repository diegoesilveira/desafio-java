package com.khipo.desafiojava.service;

import com.khipo.desafiojava.controller.dto.pedido.PedidoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PedidoService {

    PedidoDto criarPedido(PedidoDto pedidoDTO);

    Page<PedidoDto> listarPedidos(Pageable pageable);

    PedidoDto adicionarProdutoAoPedido(Long idPedido, List<Long> IdProdutos);
}
