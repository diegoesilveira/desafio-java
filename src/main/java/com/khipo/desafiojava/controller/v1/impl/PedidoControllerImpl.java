package com.khipo.desafiojava.controller.v1.impl;

import com.khipo.desafiojava.controller.dto.pedido.PedidoDto;
import com.khipo.desafiojava.controller.dto.produto.ProdutoDto;
import com.khipo.desafiojava.controller.v1.PedidoController;
import com.khipo.desafiojava.service.PedidoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@Tag(name = "Pedidos", description = "Endpoints para gerenciamento dos pedidos")
public class PedidoControllerImpl implements PedidoController {


    private final PedidoService pedidoService;

    public PedidoControllerImpl(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    /**
     * @return
     */
    @Override
    @PostMapping
    public ResponseEntity<PedidoDto> criarPedido(@Valid @RequestBody PedidoDto pedidoDto) {
        PedidoDto pedidoDTO = pedidoService.criarPedido(pedidoDto);
        return new ResponseEntity<>(pedidoDTO,HttpStatus.CREATED);
    }

    /**
     * @return
     */
    @Override
    @GetMapping
    public ResponseEntity<Page<PedidoDto>> listarPedidos(Pageable pageable) {
        Page<PedidoDto> pedidosPage = pedidoService.listarPedidos(pageable);
        return new ResponseEntity<>(pedidosPage, HttpStatus.OK);
    }

    /**
     * @param id
     * @param idProdutos
     * @return
     */
    @Override
    @PutMapping("/{id}/adicionar-produto")
    public ResponseEntity<PedidoDto> adicionarProdutoAoPedido(@PathVariable Long id, @RequestBody List<Long> idProdutos) {
        PedidoDto pedidoDto = pedidoService.adicionarProdutoAoPedido(id, idProdutos);
        return new ResponseEntity<>(pedidoDto, HttpStatus.OK);
    }
}
