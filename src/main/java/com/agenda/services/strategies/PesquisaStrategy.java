package com.agenda.services.strategies;

import com.agenda.entity.ContatoEntity;
import com.agenda.repository.ContatoRepository;

import java.util.List;

public interface PesquisaStrategy {
    boolean tipoValido(String tipoBusca);
    List<ContatoEntity> buscar(ContatoRepository repository, String valor);
}
