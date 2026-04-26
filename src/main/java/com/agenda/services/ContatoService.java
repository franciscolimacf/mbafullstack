package com.agenda.services;

import com.agenda.converters.Converter;
import com.agenda.domain.ContatoDomain;
import com.agenda.repository.ContatoRepository;
import com.agenda.services.strategies.PesquisaStrategy;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContatoService {

    private final ContatoRepository repository;
    private final Converter converter;
    private final List<PesquisaStrategy> strategies;

    public ContatoDomain Incluir (ContatoDomain domain)
    {
        if(!repository.VerificarEmail(domain.getEmail()))
            throw new DataIntegrityViolationException("Email já existente");
        var contato = repository.save(converter.ConvertDomainToEntity(domain));
        return converter.ConvertEntityToDomain(contato);
    }

    public List<ContatoDomain> Listar ()
    {
       return converter.ConvertListEntityToListDomain(repository.findAll());
    }

    public List<ContatoDomain> Buscar (String tipo, String valor){
        return strategies.stream()
                .filter(strategy -> strategy.tipoValido(tipo))
                .findFirst()
                .map(strategy -> strategy.buscar(repository, valor))
                .map(converter::ConvertListEntityToListDomain)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de busca inválido: " + tipo));

    }
}
