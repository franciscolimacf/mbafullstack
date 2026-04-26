package com.agenda.services.strategies;

import com.agenda.entity.ContatoEntity;
import com.agenda.repository.ContatoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PesquisaEmail implements PesquisaStrategy {
    @Override
    public boolean tipoValido(String tipoBusca) {
        return "email".equalsIgnoreCase(tipoBusca);
    }

    @Override
    public List<ContatoEntity> buscar(ContatoRepository repository, String valor) {
        return repository.findByEmailContainingIgnoreCase(valor);
    }
}
