package com.khipo.desafiojava.mapper;

import com.khipo.desafiojava.controller.dto.pedido.PedidoDto;
import com.khipo.desafiojava.controller.dto.produto.ProdutoDto;
import com.khipo.desafiojava.exception.MappingException;
import com.khipo.desafiojava.repository.entity.PedidoEntity;
import com.khipo.desafiojava.repository.entity.ProdutoEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.khipo.desafiojava.enums.CategoriaEnum.findByIdCategoriaEnum;
import static com.khipo.desafiojava.util.MessagemException.ERRO_MAPPER_TO_DTO;
import static com.khipo.desafiojava.util.MessagemException.ERRO_MAPPER_TO_ENTITY;

@Component
public class PedidoMapper {

    public static PedidoEntity toEntity(PedidoDto pedidoDto) {
        return PedidoEntity.builder()
                .produtos(getProdutos(pedidoDto.getProdutos()))
                .valorTotal(pedidoDto.getValorTotal())
                .build();
    }

    private static List<ProdutoEntity> getProdutos(List<ProdutoDto> produtoDtoList) {
        return Optional.ofNullable(produtoDtoList)
                .map(prd -> prd.stream()
                        .map(PedidoMapper::mapProdutos)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new MappingException(ERRO_MAPPER_TO_ENTITY));
    }

    private static ProdutoEntity mapProdutos(ProdutoDto produtoDto) {
        return ProdutoEntity.builder()
                .nome(produtoDto.getNome())
                .categoria(findByIdCategoriaEnum(produtoDto.getCategoriaId()))
                .preco(produtoDto.getPreco())
                .build();
    }

    public static PedidoDto toDto(PedidoEntity pedidoEntity) {
        return PedidoDto.builder()
                .valorTotal(pedidoEntity.getValorTotal())
                .produtos(getProdutosDto(pedidoEntity.getProdutos()))
                .build();
    }

    private static List<ProdutoDto> getProdutosDto(List<ProdutoEntity> produtoEntity) {
        return Optional.ofNullable(produtoEntity)
                .map(prd -> prd.stream()
                        .map(PedidoMapper::mapToProdutosEntity)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new MappingException(ERRO_MAPPER_TO_DTO));
    }

    private static ProdutoDto mapToProdutosEntity(ProdutoEntity produtoEntity) {
        return ProdutoDto.builder()
                .nome(produtoEntity.getNome())
                .categoriaId(produtoEntity.getCategoria().getId())
                .preco(produtoEntity.getPreco())
                .build();
    }

    public static Page<PedidoDto> toDtoPage(Page<PedidoEntity> pedidoEntityPage) {
        return pedidoEntityPage.map(PedidoMapper::toDto);
    }

}
