package com.khipo.desafiojava.repository;

import com.khipo.desafiojava.repository.entity.PedidoEntity;
import com.khipo.desafiojava.repository.entity.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {

    List<ProdutoEntity> findAllByProdutosId(Long idProduto);

    @Modifying
    @Query("UPDATE PedidoEntity p SET p.produtos = NULL WHERE :produtoId MEMBER OF p.produtos")
    void removerProdutoDosPedidos(Long produtoId);
}
