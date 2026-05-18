package com.agenda.services;

import com.agenda.converters.Converter;
import com.agenda.domain.ContatoDomain;
import com.agenda.domain.ContatoTipo;
import com.agenda.repository.ContatoRepository;
import com.agenda.services.strategies.ContatoStrategy;
import com.agenda.services.strategies.PesquisaStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContatoService implements ContatoStrategy {

    private final ContatoRepository repository;
    private final Converter converter;
    private final List<PesquisaStrategy> strategies;

    @Override
    public ContatoDomain incluir (ContatoDomain domain)
    {
        log.info("Iniciando o processo de inclusão para o contato: {}", domain.getNome());

        if(repository.existsByEmail(domain.getEmail())){
            throw new IllegalArgumentException("Email já existente: " + domain.getEmail());
        }

        var contato = repository.save(converter.ConvertDomainToEntity(domain));
        log.info("Contato com ID = {} salvo com sucesso!", contato.getId());
        return converter.ConvertEntityToDomain(contato);
    }

    @Override
    public List<ContatoDomain> listar () {
        log.info("Iniciando processo de listar todos os contatos.");
        return converter.ConvertListEntityToListDomain(repository.findAll());
    }

    @Override
    public List<ContatoDomain> pesquisar (String tipo, String valor){
        log.info("Iniciando processo de pesquisa de contato. tipo = {} e valor = {}", tipo, valor);
        return strategies.stream()
                .filter(strategy -> strategy.tipoValido(tipo))
                .findFirst()
                .map(strategy -> strategy.buscar(repository, valor))
                .map(converter::ConvertListEntityToListDomain)
                .orElseThrow(() -> new IllegalStateException("Tipo de busca inválido: " + tipo));

    }

    @Override
    public ContatoDomain editar(Long id, ContatoDomain domain) {
        log.info("Iniciando processo de edição de contato de ID = {}", id);
        var entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contato não encontrado com id: " + id));
        if(repository.existsByEmailAndIdNot(domain.getEmail(), entity.getId())) {
            throw new IllegalArgumentException("Email já existente: " + domain.getEmail());
        }
        var entityEditada = converter.atualizarEntity(entity, domain);
        log.info("Contato com ID = {} editado com sucesso!", id);

        return converter.ConvertEntityToDomain(repository.save(entityEditada));
    }

    @Override
    public void excluir(Long id) {
        log.info("Iniciando processo de exclusão do contato de ID = {}", id);
        var entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contato não encontrado com id: " + id));

        if(entity.getTipo() == ContatoTipo.FAMILIA)
            throw new IllegalStateException("Não pode excluir contato do tipo FAMILIA");

        log.info("Contato com ID = {} excluído com sucesso!", id);
        repository.deleteById(id);
    }
}
