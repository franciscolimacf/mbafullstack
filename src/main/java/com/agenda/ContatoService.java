package com.agenda;

import com.agenda.converters.Converter;
import com.agenda.domain.ContatoDomain;
import com.agenda.repository.ContatoRepository;
import com.agenda.services.strategies.ContatoStrategy;
import com.agenda.services.strategies.PesquisaStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContatoService implements ContatoStrategy {

    private final ContatoRepository repository;
    private final Converter converter;
    private final List<PesquisaStrategy> strategies;

    @Override
    public ContatoDomain incluir (ContatoDomain domain)
    {
        if(!repository.VerificarEmail(domain.getEmail()))
            throw new DataIntegrityViolationException("Email já existente");
        var contato = repository.save(converter.ConvertDomainToEntity(domain));
        return converter.ConvertEntityToDomain(contato);
    }

    @Override
    public List<ContatoDomain> listar ()
    {
       return converter.ConvertListEntityToListDomain(repository.findAll());
    }

    @Override
    public List<ContatoDomain> buscar (String tipo, String valor){
        return strategies.stream()
                .filter(strategy -> strategy.tipoValido(tipo))
                .findFirst()
                .map(strategy -> strategy.buscar(repository, valor))
                .map(converter::ConvertListEntityToListDomain)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de busca inválido: " + tipo));

    }
}
