package com.agenda.app.adapters.output;

import com.agenda.app.application.domain.ContatoDomain;
import com.agenda.app.application.port.out.IContatoRepositoryPort;
import com.agenda.app.adapters.converters.Converter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ContatoRepositoryAdapter implements IContatoRepositoryPort {
    private final ContatoRepository repository;
    private final Converter converter;

    @Override
    public boolean existsByEmail(String email){
        return repository.existsByEmail(email);
    }

    public ContatoDomain save(ContatoDomain domain){
        return converter.ConvertEntityToDomain(repository.save(converter.ConvertDomainToEntity(domain)));
    }
}
