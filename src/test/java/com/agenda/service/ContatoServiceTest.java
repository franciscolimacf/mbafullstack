package com.agenda.service;

import com.agenda.converters.Converter;
import com.agenda.domain.ContatoDomain;
import com.agenda.domain.ContatoTipo;
import com.agenda.entity.ContatoEntity;
import com.agenda.repository.ContatoRepository;
import com.agenda.services.ContatoService;
import com.agenda.services.strategies.PesquisaStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ContatoServiceTest {

    @Mock
    private ContatoRepository repository;

    @Mock
    private Converter converter;

    @Mock
    private PesquisaStrategy strategy;

    @InjectMocks
    private ContatoService service;

    private ContatoDomain domain;
    private ContatoEntity entity;

    @BeforeEach
    void setUp() {
        domain = ContatoDomain.builder()
                .nome("Francisco Lima")
                .email("francisco@email.com")
                .telefone("83999990000")
                .endereco("Rua A, 1")
                .idade(27)
                .tipo(ContatoTipo.AMIGO)
                .build();

        entity = ContatoEntity.builder()
                .id(1L)
                .nome("Francisco Lima")
                .email("francisco@email.com")
                .telefone("83999990000")
                .endereco("Rua A, 1")
                .idade(27)
                .tipo(ContatoTipo.AMIGO)
                .dataCad(LocalDateTime.now())
                .ativo(true)
                .build();

        service = new ContatoService(repository, converter, List.of(strategy));
    }

    @Test
    void incluir_emailNovo_salvaNaBase() {
        given(repository.existsByEmail(domain.getEmail())).willReturn(false);
        given(converter.ConvertDomainToEntity(domain)).willReturn(entity);
        given(repository.save(entity)).willReturn(entity);
        given(converter.ConvertEntityToDomain(entity)).willReturn(domain);

        var resultado = service.incluir(domain);

        assertThat(resultado).isEqualTo(domain);
        then(repository).should().save(entity);
    }

    @Test
    void incluir_emailDuplicado_lancaIllegalArgumentException() {
        given(repository.existsByEmail(domain.getEmail())).willReturn(true);

        assertThatThrownBy(() -> service.incluir(domain))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email já existente");

        then(repository).should(never()).save(any());
    }

    @Test
    void listar_retornaListaConvertida() {
        given(repository.findAll()).willReturn(List.of(entity));
        given(converter.ConvertListEntityToListDomain(List.of(entity))).willReturn(List.of(domain));

        var resultado = service.listar();

        assertThat(resultado).hasSize(1).containsExactly(domain);
    }

    @Test
    void listar_baseVazia_retornaListaVazia() {
        given(repository.findAll()).willReturn(List.of());
        given(converter.ConvertListEntityToListDomain(List.of())).willReturn(List.of());

        assertThat(service.listar()).isEmpty();
    }

    @Test
    void pesquisar_tipoValido_delegaParaStrategy() {
        given(strategy.tipoValido("nome")).willReturn(true);
        given(strategy.buscar(repository, "Francisco")).willReturn(List.of(entity));
        given(converter.ConvertListEntityToListDomain(List.of(entity))).willReturn(List.of(domain));

        var resultado = service.pesquisar("nome", "Francisco");

        assertThat(resultado).containsExactly(domain);
    }

    @Test
    void pesquisar_tipoInvalido_lancaIllegalStateException() {
        given(strategy.tipoValido("x")).willReturn(false);

        assertThatThrownBy(() -> service.pesquisar("x", "qualquer"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Tipo de busca inválido");
    }

    @Test
    void editar_idExistenteEmailLivre_salvaAlteracao() {
        given(repository.findById(1L)).willReturn(Optional.of(entity));
        given(repository.existsByEmailAndIdNot(domain.getEmail(), entity.getId())).willReturn(false);
        given(converter.atualizarEntity(entity, domain)).willReturn(entity);
        given(repository.save(entity)).willReturn(entity);
        given(converter.ConvertEntityToDomain(entity)).willReturn(domain);

        var resultado = service.editar(1L, domain);

        assertThat(resultado).isEqualTo(domain);
        then(repository).should().save(entity);
    }

    @Test
    void editar_idNaoEncontrado_lancaRuntimeException() {
        given(repository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.editar(99L, domain))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Contato não encontrado");
    }

    @Test
    void editar_emailDuplicadoEmOutroContato_lancaIllegalArgumentException() {
        given(repository.findById(1L)).willReturn(Optional.of(entity));
        given(repository.existsByEmailAndIdNot(domain.getEmail(), entity.getId())).willReturn(true);

        assertThatThrownBy(() -> service.editar(1L, domain))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email já existente");
    }

    @Test
    void excluir_idExistenteTipoNaoFamilia_deletaDaBase() {
        given(repository.findById(1L)).willReturn(Optional.of(entity));

        service.excluir(1L);

        then(repository).should().deleteById(1L);
    }

    @Test
    void excluir_idNaoEncontrado_lancaRuntimeException() {
        given(repository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.excluir(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Contato não encontrado");
    }

    @Test
    void excluir_tipoFamilia_lancaIllegalStateException() {
        var entityFamilia = ContatoEntity.builder()
                .id(2L)
                .nome("Pai")
                .email("pai@email.com")
                .tipo(ContatoTipo.FAMILIA)
                .build();

        given(repository.findById(2L)).willReturn(Optional.of(entityFamilia));

        assertThatThrownBy(() -> service.excluir(2L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("FAMILIA");

        then(repository).should(never()).deleteById(any());
    }
}
