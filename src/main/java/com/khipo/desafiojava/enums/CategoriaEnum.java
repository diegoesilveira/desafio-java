package com.khipo.desafiojava.enums;

import com.khipo.desafiojava.exception.MappingException;

import static com.khipo.desafiojava.util.MessagemException.ERRO_CATEGORIA_INVALIDA;

public enum CategoriaEnum {

    BEBIDA(1, "Bebida"),
    ENTRADA(2, "Entrada"),
    PRATO_PRINCIPAL(3, "Prato Principal"),
    SOBREMESA(4, "Sobremesa");

    private final int id;
    private final String descricao;

    CategoriaEnum(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public static CategoriaEnum findByIdCategoriaEnum(int categoriaId) {
        switch (categoriaId) {
            case 1:
                return CategoriaEnum.BEBIDA;
            case 2:
                return CategoriaEnum.ENTRADA;
            case 3:
                return CategoriaEnum.PRATO_PRINCIPAL;
            case 4:
                return CategoriaEnum.SOBREMESA;
            default:
                throw new MappingException(ERRO_CATEGORIA_INVALIDA + categoriaId);
        }
    }
}
