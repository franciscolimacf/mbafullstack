package com.agenda.services.strategies;

import com.agenda.domain.ContatoTipo;
import com.agenda.entity.ContatoEntity;
import com.agenda.repository.ContatoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PesquisaTipo implements PesquisaStrategy{
    @Override
    public boolean tipoValido(String tipoBusca) {
        return "tipo".equalsIgnoreCase(tipoBusca);
    }

    @Override
    public List<ContatoEntity> buscar(ContatoRepository repository, String valor) {
        try
        {
            var contatoTipo = ContatoTipo.valueOf(valor);
            return repository.findByTipo(contatoTipo);
        } catch (Exception e) {
            throw new IllegalArgumentException("Tipo de contato inválido");
        }
    }
}
