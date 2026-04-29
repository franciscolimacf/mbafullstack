package com.agenda.services.strategies;

import com.agenda.domain.ContatoDomain;

import java.util.List;

public interface ContatoStrategy {
    ContatoDomain incluir(ContatoDomain domain);
     List<ContatoDomain> listar();
     List<ContatoDomain> pesquisar(String tipo, String valor);
    ContatoDomain editar(Long id, ContatoDomain domain);
    void excluir(Long id);
}
