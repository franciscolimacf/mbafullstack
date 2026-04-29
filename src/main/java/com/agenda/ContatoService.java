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
            throw new IllegalArgumentException("Email já existente: " + domain.getEmail());
        var contato = repository.save(converter.ConvertDomainToEntity(domain));
        return converter.ConvertEntityToDomain(contato);
    }

    @Override
    public List<ContatoDomain> listar ()
    {
       return converter.ConvertListEntityToListDomain(repository.findAll());
    }

    @Override
    public List<ContatoDomain> pesquisar (String tipo, String valor){
        return strategies.stream()
                .filter(strategy -> strategy.tipoValido(tipo))
                .findFirst()
                .map(strategy -> strategy.buscar(repository, valor))
                .map(converter::ConvertListEntityToListDomain)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de busca inválido: " + tipo));

    }

    @Override
    public ContatoDomain editar(Long id, ContatoDomain domain) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contato não encontrado com id: " + id));
                converter.atualizarEntity(entity, domain);
                return converter.ConvertEntityToDomain(repository.save(entity));
    }

    @Override
    public void excluir(Long id) {

    }
}
