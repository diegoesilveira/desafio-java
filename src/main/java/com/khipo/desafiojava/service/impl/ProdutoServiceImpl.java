package com.khipo.desafiojava.service.impl;

import com.khipo.desafiojava.controller.dto.produto.ProdutoDto;
import com.khipo.desafiojava.exception.DadoNaoEncontradoException;
import com.khipo.desafiojava.repository.PedidoRepository;
import com.khipo.desafiojava.repository.ProdutoRepository;
import com.khipo.desafiojava.repository.entity.ProdutoEntity;
import com.khipo.desafiojava.service.ProdutoService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.khipo.desafiojava.enums.CategoriaEnum.findByIdCategoriaEnum;
import static com.khipo.desafiojava.mapper.ProdutoMapper.*;
import static com.khipo.desafiojava.util.MessagemException.PRODUTO_NAO_ENCONTRADO;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final PedidoRepository pedidoRepository;

    public ProdutoServiceImpl(ProdutoRepository produtoRepository, PedidoRepository pedidoRepository) {
        this.produtoRepository = produtoRepository;
        this.pedidoRepository = pedidoRepository;
    }


    /**
     * @param produtoDto
     * @return
     */
    @Override
    public ProdutoDto criarProduto(ProdutoDto produtoDto) {
        ProdutoEntity produtoEntity = produtoRepository.save(toEntity(produtoDto));
        return toDTO(produtoEntity);
    }

    /**
     * @return
     */
    @Override
    public Page<ProdutoDto> listarProdutos(Pageable pageable) {
        Page<ProdutoEntity> produtoEntityPage = produtoRepository.findAll(pageable);
        return toDtoPage(produtoEntityPage);

    }

    /**
     * @param id
     * @param produtoDTO
     * @return
     */
    @Override
    @Transactional
    public ProdutoDto atualizarProduto(Long id, ProdutoDto produtoDTO) {

        ProdutoEntity produtoEntity = produtoRepository.findById(id)
                .orElseThrow(() -> new DadoNaoEncontradoException(PRODUTO_NAO_ENCONTRADO));

        produtoEntity.setNome(produtoDTO.getNome());
        produtoEntity.setCategoria(findByIdCategoriaEnum(produtoDTO.getCategoriaId()));
        produtoEntity.setPreco(produtoDTO.getPreco());

        ProdutoEntity saved = produtoRepository.save(produtoEntity);
        return toDTO(saved);
    }

    /**
     * @param id
     */
    @Override
    public void deletarProduto(Long id) {
        ProdutoEntity produtoEntity = produtoRepository.findById(id)
                .orElseThrow(() -> new DadoNaoEncontradoException(PRODUTO_NAO_ENCONTRADO));

        produtoRepository.deleteById(produtoEntity.getId());
    }
}
