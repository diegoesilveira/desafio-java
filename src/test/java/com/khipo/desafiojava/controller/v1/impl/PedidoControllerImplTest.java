package com.khipo.desafiojava.controller.v1.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khipo.desafiojava.controller.dto.pedido.PedidoDto;
import com.khipo.desafiojava.controller.dto.produto.ProdutoDto;
import com.khipo.desafiojava.repository.entity.UserEntity;
import com.khipo.desafiojava.service.PedidoService;
import com.khipo.desafiojava.service.auth.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class PedidoControllerImplTest {

    private MockMvc mockMvc;
    @MockBean
    private PedidoService pedidoService;

    @MockBean
    private TokenService tokenService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testCriarPedido() throws Exception {
        PedidoDto pedidoDto = new PedidoDto();
        when(pedidoService.criarPedido(pedidoDto)).thenReturn(pedidoDto);

        // Mock do TokenService para retornar um token válido
        when(tokenService.generateToken(any(UserEntity.class))).thenReturn("token");

        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedidoDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testErroCriarPedido() throws Exception {
        PedidoDto pedidoDto = new PedidoDto();

        when(pedidoService.criarPedido(pedidoDto)).thenThrow(new RuntimeException("Erro ao criar pedido"));

        when(tokenService.generateToken(any(UserEntity.class))).thenReturn("token");

        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testListarPedidosComSucesso() throws Exception {
        // Simular uma lista de pedidos retornada pelo serviço
        List<ProdutoDto> produto = new ArrayList<>();
        List<PedidoDto> listaPedidos = Arrays.asList(
                new PedidoDto(1L,produto, BigDecimal.valueOf(10)),
                new PedidoDto(2L, produto, BigDecimal.valueOf(15)),
                new PedidoDto(3L, produto, BigDecimal.valueOf(10))
        );
        Page<PedidoDto> pedidosPage = new PageImpl<>(listaPedidos);

        // Configurar o mock do PedidoService para retornar a lista de pedidos simulada
        when(pedidoService.listarPedidos(any(Pageable.class))).thenReturn(pedidosPage);

        // Mock do TokenService para retornar um token válido
        when(tokenService.generateToken(any(UserEntity.class))).thenReturn("token");

        // Executar a requisição GET para listar os pedidos
        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk()) // Espera-se que o status retornado seja 200 (OK)
                .andExpect(jsonPath("$.content", hasSize(3))) // Verificar se a resposta contém 3 pedidos
                .andExpect(jsonPath("$.content[0].id").value(1)); // Verificar o ID do primeiro pedido
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testErroListarPedidos() throws Exception {

        when(pedidoService.listarPedidos(any(Pageable.class))).thenThrow(new RuntimeException("Erro ao listar pedidos"));

        when(tokenService.generateToken(any(UserEntity.class))).thenReturn("token");

        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testAdicionarProdutoAoPedidoComSucesso() throws Exception {
        Long idPedido = 1L;
        List<Long> idProdutos = Arrays.asList(1L, 2L); // IDs dos produtos a serem adicionados

        PedidoDto pedidoDto = new PedidoDto();
        pedidoDto.setId(idPedido);
        pedidoDto.setProdutos(Arrays.asList(
                new ProdutoDto(1L, "Produto 1", BigDecimal.valueOf(10.0), 3),
                new ProdutoDto(2L, "Produto 2", BigDecimal.valueOf(20.0), 3)
        ));

        when(pedidoService.adicionarProdutoAoPedido(idPedido, idProdutos)).thenReturn(pedidoDto);

        when(tokenService.generateToken(any(UserEntity.class))).thenReturn("token");

        mockMvc.perform(put("/pedidos/{id}/adicionar-produto", idPedido)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(idProdutos)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idPedido))
                .andExpect(jsonPath("$.produtos", hasSize(2)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testErroAdicionarProdutoAoPedido() throws Exception {
        Long idPedido = 1L;
        List<Long> idProdutos = Arrays.asList(1L, 2L);

        when(pedidoService.adicionarProdutoAoPedido(idPedido, idProdutos)).thenThrow(new RuntimeException("Erro ao adicionar produtos ao pedido"));

        when(tokenService.generateToken(any(UserEntity.class))).thenReturn("token");

        mockMvc.perform(put("/pedidos/{id}/adicionar-produto", idPedido)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(idProdutos)))
                .andExpect(status().isInternalServerError());
    }
}