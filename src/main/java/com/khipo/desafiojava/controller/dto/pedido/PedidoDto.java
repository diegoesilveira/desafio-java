package com.khipo.desafiojava.controller.dto.pedido;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khipo.desafiojava.controller.dto.produto.ProdutoDto;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PedidoDto {

    private Long id;
    private List<ProdutoDto> produtos;

    @DecimalMin(value = "0.01", message = "O valor total deve ser maior que zero")
    private BigDecimal valorTotal;

}
