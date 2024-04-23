package com.khipo.desafiojava.repository.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@Builder
public class PedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A lista de produtos não pode ser nula")
    @OneToMany(cascade = CascadeType.ALL)
    private List<ProdutoEntity> produtos;

    private BigDecimal valorTotal;

    public PedidoEntity() {
        this.produtos = new ArrayList<>();
        this.valorTotal = BigDecimal.ZERO;
    }

    public void adicionarProduto(List<ProdutoEntity> produtos) {
        BigDecimal total = produtos.stream()
                .map(ProdutoEntity::getPreco)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        valorTotal = valorTotal.add(total);
    }


}
