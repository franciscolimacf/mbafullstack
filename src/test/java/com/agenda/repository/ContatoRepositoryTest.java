package com.agenda.repository;

import com.agenda.domain.ContatoTipo;
import com.agenda.entity.ContatoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ContatoRepositoryTest {

    @Autowired
    private ContatoRepository repository;

    private Long idFrancisco;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        var francisco = repository.save(ContatoEntity.builder()
                .nome("Francisco Lima").email("francisco@email.com").telefone("83999990000")
                .endereco("Rua A, 1").idade(27).tipo(ContatoTipo.AMIGO)
                .dataCad(LocalDateTime.now()).ativo(true).build());

        repository.save(ContatoEntity.builder()
                .nome("Amanda Família").email("amanda@email.com").telefone("83999991111")
                .endereco("Rua B, 2").idade(23).tipo(ContatoTipo.FAMILIA)
                .dataCad(LocalDateTime.now()).ativo(true).build());

        repository.save(ContatoEntity.builder()
                .nome("João Trabalho").email("joao@email.com").telefone("83999992222")
                .endereco("Rua C, 3").idade(28).tipo(ContatoTipo.TRABALHO)
                .dataCad(LocalDateTime.now()).ativo(true).build());

        idFrancisco = francisco.getId();
    }

    @Test
    void findByNomeContainingIgnoreCase_parcialMinusculo_encontra() {
        var resultado = repository.findByNomeContainingIgnoreCase("francisco");
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("Francisco Lima");
    }

    @Test
    void findByNomeContainingIgnoreCase_parcialMaiusculo_encontra() {
        var resultado = repository.findByNomeContainingIgnoreCase("LIMA");
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("Francisco Lima");
    }

    @Test
    void findByEmailContainingIgnoreCase_dominioComum_retornaTodos() {
        var resultado = repository.findByEmailContainingIgnoreCase("email.com");
        assertThat(resultado).hasSize(3);
    }

    @Test
    void findByTelefoneContaining_parcialUnico_retornaApenas() {
        var resultado = repository.findByTelefoneContaining("000");
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("Francisco Lima");
    }

    @Test
    void findByTipo_familia_retornaApenasAmanda() {
        var resultado = repository.findByTipo(ContatoTipo.FAMILIA);
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("Amanda Família");
    }

    @Test
    void existsByEmail_emailExistente_retornaTrue() {
        assertThat(repository.existsByEmail("francisco@email.com")).isTrue();
    }

    @Test
    void existsByEmail_emailInexistente_retornaFalse() {
        assertThat(repository.existsByEmail("naoexiste@email.com")).isFalse();
    }

    @Test
    void existsByEmailAndIdNot_mesmoRegistro_retornaFalse() {
        assertThat(repository.existsByEmailAndIdNot("francisco@email.com", idFrancisco)).isFalse();
    }

    @Test
    void existsByEmailAndIdNot_emailDeOutro_retornaTrue() {
        var outroId = repository.findByNomeContainingIgnoreCase("Amanda").get(0).getId();
        assertThat(repository.existsByEmailAndIdNot("francisco@email.com", outroId)).isTrue();
    }
}
