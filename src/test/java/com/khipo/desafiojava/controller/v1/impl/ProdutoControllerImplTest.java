package com.khipo.desafiojava.controller.v1.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khipo.desafiojava.controller.dto.produto.ProdutoDto;
import com.khipo.desafiojava.service.ProdutoService;
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
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ProdutoControllerImplTest {

    private MockMvc mockMvc;
    @MockBean
    private ProdutoService produtoService;

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
    void testCriarProdutoComSucesso() throws Exception {
        ProdutoDto produtoDto = new ProdutoDto();
        produtoDto.setNome("Produto Teste");
        produtoDto.setPreco(BigDecimal.valueOf(10.00));

        when(produtoService.criarProduto(any(ProdutoDto.class))).thenReturn(produtoDto);

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Produto Teste"))
                .andExpect(jsonPath("$.preco").value(10.0));

        verify(produtoService, times(1)).criarProduto(any(ProdutoDto.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testCriarProdutoComErro() throws Exception {
        ProdutoDto produtoDto = new ProdutoDto();
        produtoDto.setNome("Produto Teste");
        produtoDto.setPreco(BigDecimal.valueOf(10.0));

        // Configurar o mock do ProdutoService para lançar uma exceção ao criar o produto
        when(produtoService.criarProduto(any(ProdutoDto.class))).thenThrow(new RuntimeException("Erro ao criar produto"));

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoDto)))
                .andExpect(status().isInternalServerError());

        verify(produtoService, times(1)).criarProduto(any(ProdutoDto.class));
    }
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testListarProdutosComSucesso() throws Exception {
        List<ProdutoDto> produtos = Arrays.asList(
                new ProdutoDto(1L, "Produto 1", BigDecimal.valueOf(10.0), 3),
                new ProdutoDto(2L, "Produto 2", BigDecimal.valueOf(20.0),3)
        );
        Page<ProdutoDto> produtosPage = new PageImpl<>(produtos);

        when(produtoService.listarProdutos(any(Pageable.class))).thenReturn(produtosPage);

        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].nome").value("Produto 1"))
                .andExpect(jsonPath("$.content[1].nome").value("Produto 2"));

        verify(produtoService, times(1)).listarProdutos(any(Pageable.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testListarProdutosComErro() throws Exception {

        when(produtoService.listarProdutos(any(Pageable.class))).thenThrow(new RuntimeException("Erro ao listar produtos"));

        mockMvc.perform(get("/produtos"))
                .andExpect(status().isInternalServerError());

        verify(produtoService, times(1)).listarProdutos(any(Pageable.class));
    }
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testAtualizarProdutoComSucesso() throws Exception {
        Long idProduto = 1L;
        ProdutoDto produtoDto = new ProdutoDto();
        produtoDto.setNome("Produto Atualizado");
        produtoDto.setPreco(BigDecimal.valueOf(15.0));

        when(produtoService.atualizarProduto(eq(idProduto), any(ProdutoDto.class))).thenReturn(produtoDto);

        mockMvc.perform(put("/produtos/{id}", idProduto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Produto Atualizado"))
                .andExpect(jsonPath("$.preco").value(15.0));

        verify(produtoService, times(1)).atualizarProduto(eq(idProduto), any(ProdutoDto.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testAtualizarProdutoComErro() throws Exception {
        Long idProduto = 1L;
        ProdutoDto produtoDto = new ProdutoDto();
        produtoDto.setNome("Produto Atualizado");
        produtoDto.setPreco(BigDecimal.valueOf(15.0));

        when(produtoService.atualizarProduto(eq(idProduto), any(ProdutoDto.class))).thenThrow(new RuntimeException("Erro ao atualizar produto"));

        mockMvc.perform(put("/produtos/{id}", idProduto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoDto)))
                .andExpect(status().isInternalServerError());

        verify(produtoService, times(1)).atualizarProduto(eq(idProduto), any(ProdutoDto.class));
    }
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testDeletarProdutoComSucesso() throws Exception {
        Long idProduto = 1L;

        mockMvc.perform(delete("/produtos/{id}", idProduto))
                .andExpect(status().isNoContent());

        verify(produtoService, times(1)).deletarProduto(eq(idProduto));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testDeletarProdutoComErro() throws Exception {
        Long idProduto = 1L;

        doThrow(new RuntimeException("Erro ao deletar produto")).when(produtoService).deletarProduto(eq(idProduto));

        mockMvc.perform(delete("/produtos/{id}", idProduto))
                .andExpect(status().isInternalServerError());

        verify(produtoService, times(1)).deletarProduto(eq(idProduto));
    }
}