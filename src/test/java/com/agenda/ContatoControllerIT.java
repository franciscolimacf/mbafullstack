package com.agenda;

import com.agenda.domain.ContatoTipo;
import com.agenda.dtos.ContatoRequest;
import com.agenda.entity.ContatoEntity;
import com.agenda.repository.ContatoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ContatoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ContatoRepository repository;

    @BeforeEach
    void limparBase() {
        repository.deleteAll();
    }

    private ContatoRequest requestValido() {
        var req = new ContatoRequest();
        req.setNome("Francisco Lima");
        req.setEmail("francisco@email.com");
        req.setTelefone("83999990000");
        req.setEndereco("Rua A, 1");
        req.setIdade(27);
        req.setTipo(ContatoTipo.AMIGO);
        return req;
    }

    @Test
    void fluxoCrud_completo() throws Exception {
        var respIncluir = mockMvc.perform(post("/contatos/incluir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestValido())))
                .andExpect(status().isOk())
                .andReturn();

        String body = respIncluir.getResponse().getContentAsString();
        Long id = Long.parseLong(body.replaceAll("[^0-9]", ""));

        mockMvc.perform(get("/contatos/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        mockMvc.perform(get("/contatos/pesquisar").param("tipo", "nome").param("valor", "Francisco"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nome").value("Francisco Lima"));

        var reqEditado = requestValido();
        reqEditado.setNome("Francisco Lima");
        mockMvc.perform(put("/contatos/editar/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqEditado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Francisco Lima"));

        mockMvc.perform(delete("/contatos/excluir/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/contatos/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void incluir_emailDuplicado_retorna409() throws Exception {
        mockMvc.perform(post("/contatos/incluir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestValido())))
                .andExpect(status().isOk());

        mockMvc.perform(post("/contatos/incluir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestValido())))
                .andExpect(status().isConflict());
    }

    @Test
    void editar_contatoInexistente_retorna404() throws Exception {
        mockMvc.perform(put("/contatos/editar/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestValido())))
                .andExpect(status().isNotFound());
    }

    @Test
    void excluir_tipoFamilia_retorna400() throws Exception {
        var entityFamilia = repository.save(ContatoEntity.builder()
                .nome("Pai").email("pai@email.com").telefone("83999991111")
                .endereco("Rua B, 2").idade(70).tipo(ContatoTipo.FAMILIA)
                .dataCad(LocalDateTime.now()).ativo(true).build());

        mockMvc.perform(delete("/contatos/excluir/" + entityFamilia.getId()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void incluir_semNome_retorna400() throws Exception {
        var req = requestValido();
        req.setNome("");

        mockMvc.perform(post("/contatos/incluir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void pesquisar_porEmail_retornaContato() throws Exception {
        mockMvc.perform(post("/contatos/incluir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestValido())))
                .andExpect(status().isOk());

        mockMvc.perform(get("/contatos/pesquisar").param("tipo", "email").param("valor", "francisco"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void pesquisar_porTipoEnum_retornaContato() throws Exception {
        mockMvc.perform(post("/contatos/incluir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestValido())))
                .andExpect(status().isOk());

        mockMvc.perform(get("/contatos/pesquisar").param("tipo", "tipo").param("valor", "AMIGO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void pesquisar_tipoInvalido_retorna400() throws Exception {
        mockMvc.perform(get("/contatos/pesquisar").param("tipo", "invalido").param("valor", "x"))
                .andExpect(status().isBadRequest());
    }
}
