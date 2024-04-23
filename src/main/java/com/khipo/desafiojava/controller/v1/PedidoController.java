package com.khipo.desafiojava.controller.v1;

import com.khipo.desafiojava.controller.dto.pedido.PedidoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PedidoController {

    ResponseEntity<PedidoDto> criarPedido(PedidoDto pedidoDTO);

    ResponseEntity<Page<PedidoDto>> listarPedidos(Pageable pageable);

    ResponseEntity<PedidoDto> adicionarProdutoAoPedido(Long id, List<Long> idProdutos);

}
