package com.khipo.desafiojava.mapper;

import com.khipo.desafiojava.controller.dto.produto.ProdutoDto;
import com.khipo.desafiojava.repository.entity.ProdutoEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import static com.khipo.desafiojava.enums.CategoriaEnum.findByIdCategoriaEnum;

@Component
public class ProdutoMapper {

    public static ProdutoEntity toEntity(ProdutoDto produtoDTO) {
        return ProdutoEntity.builder()
                .id(produtoDTO.getId())
                .categoria(findByIdCategoriaEnum(produtoDTO.getCategoriaId()))
                .preco(produtoDTO.getPreco())
                .nome(produtoDTO.getNome())
                .build();

    }

    public static ProdutoDto toDTO(ProdutoEntity produto) {
        return ProdutoDto.builder()
                .id(produto.getId())
                .nome(produto.getNome())
                .categoriaId(produto.getCategoria().getId())
                .preco(produto.getPreco())
                .build();
    }

    public static Page<ProdutoDto> toDtoPage(Page<ProdutoEntity> produtoEntityPage) {
        return produtoEntityPage.map(ProdutoMapper::toDTO);
    }
}
