package com.agenda.controller;

import com.agenda.converters.Converter;
import com.agenda.domain.ContatoDomain;
import com.agenda.domain.ContatoTipo;
import com.agenda.dtos.ContatoRequest;
import com.agenda.dtos.ContatoResponse;
import com.agenda.services.strategies.ContatoStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContatoController.class)
class ContatoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ContatoStrategy strategy;

    @MockBean
    private Converter converter;

    private ContatoRequest requestValido() {
        var req = new ContatoRequest();
        req.setNome("Francisco Lima");
        req.setEmail("francisco@email.com");
        req.setTelefone("83999990000");
        req.setEndereco("Rua A, 1");
        req.setIdade(30);
        req.setTipo(ContatoTipo.AMIGO);
        return req;
    }

    private ContatoDomain domain() {
        return ContatoDomain.builder()
                .id(1L).nome("Francisco Lima").email("francisco@email.com")
                .telefone("83999990000").endereco("Rua A, 1")
                .idade(30).tipo(ContatoTipo.AMIGO).dataCad(LocalDateTime.now())
                .build();
    }

    private ContatoResponse response() {
        return ContatoResponse.builder()
                .id(1L).nome("Francisco Lima").email("francisco@email.com")
                .telefone("83999990000").endereco("Rua A, 1")
                .idade(30).tipo(ContatoTipo.AMIGO).dataCad(LocalDateTime.now())
                .build();
    }

    @Test
    void incluir_requestValido_retorna200() throws Exception {
        given(converter.ConvertRequestToDomain(any())).willReturn(domain());
        given(strategy.incluir(any())).willReturn(domain());
        given(converter.ConvertDomainToResponse(any())).willReturn(response());

        mockMvc.perform(post("/contatos/incluir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestValido())))
                .andExpect(status().isOk());
    }

    @Test
    void incluir_nomeEmBranco_retorna400() throws Exception {
        var req = requestValido();
        req.setNome("");

        mockMvc.perform(post("/contatos/incluir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void incluir_emailInvalido_retorna400() throws Exception {
        var req = requestValido();
        req.setEmail("nao eh email");

        mockMvc.perform(post("/contatos/incluir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void incluir_idadeNegativa_retorna400() throws Exception {
        var req = requestValido();
        req.setIdade(-1);

        mockMvc.perform(post("/contatos/incluir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void incluir_tipoNulo_retorna400() throws Exception {
        var req = requestValido();
        req.setTipo(null);

        mockMvc.perform(post("/contatos/incluir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listar_retorna200ComArray() throws Exception {
        given(strategy.listar()).willReturn(List.of(domain()));
        given(converter.ConvertListDomainToListResponse(any())).willReturn(List.of(response()));

        mockMvc.perform(get("/contatos/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void pesquisar_tipoValido_retorna200() throws Exception {
        given(strategy.pesquisar("nome", "Ana")).willReturn(List.of(domain()));
        given(converter.ConvertListDomainToListResponse(any())).willReturn(List.of(response()));

        mockMvc.perform(get("/contatos/pesquisar").param("tipo", "nome").param("valor", "Francisco"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void pesquisar_tipoInvalido_retorna400() throws Exception {
        given(strategy.pesquisar("x", "qualquer"))
                .willThrow(new IllegalStateException("Tipo de busca inválido: x"));

        mockMvc.perform(get("/contatos/pesquisar").param("tipo", "x").param("valor", "qualquer"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void editar_idExistente_retorna200() throws Exception {
        given(converter.ConvertRequestToDomain(any())).willReturn(domain());
        given(strategy.editar(eq(1L), any())).willReturn(domain());
        given(converter.ConvertDomainToResponse(any())).willReturn(response());

        mockMvc.perform(put("/contatos/editar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestValido())))
                .andExpect(status().isOk());
    }

    @Test
    void editar_idInexistente_retorna404() throws Exception {
        given(converter.ConvertRequestToDomain(any())).willReturn(domain());
        given(strategy.editar(eq(99L), any()))
                .willThrow(new RuntimeException("Contato não encontrado com id: 99"));

        mockMvc.perform(put("/contatos/editar/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestValido())))
                .andExpect(status().isNotFound());
    }

    @Test
    void excluir_idExistente_retorna204() throws Exception {
        willDoNothing().given(strategy).excluir(1L);

        mockMvc.perform(delete("/contatos/excluir/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void excluir_tipoFamilia_retorna400() throws Exception {
        willThrow(new IllegalStateException("Não pode excluir contato do tipo FAMILIA"))
                .given(strategy).excluir(2L);

        mockMvc.perform(delete("/contatos/excluir/2"))
                .andExpect(status().isBadRequest());
    }
}
