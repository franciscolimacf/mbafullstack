package com.agenda.services.strategies;

import com.agenda.entity.ContatoEntity;
import com.agenda.repository.ContatoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PesquisaNome implements PesquisaStrategy{
    @Override
    public boolean tipoValido(String tipoBusca) {
        return "nome".equalsIgnoreCase(tipoBusca);
    }

    @Override
    public List<ContatoEntity> buscar(ContatoRepository repository, String valor) {
        return repository.findByNomeContainingIgnoreCase(valor);
    }
}
