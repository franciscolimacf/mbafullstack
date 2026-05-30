package com.agenda.app.application.usecase;

import com.agenda.app.application.port.in.IContatoServicePort;
import com.agenda.app.application.port.out.IContatoRepositoryPort;
import com.agenda.app.application.domain.ContatoDomain;
import com.agenda.app.utils.UseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@UseCase
@Slf4j
@RequiredArgsConstructor
public class ContatoServiceUseCase implements IContatoServicePort {

    private final IContatoRepositoryPort repository;

    @Override
    public ContatoDomain incluir (ContatoDomain domain)
    {
        log.info("Iniciando o processo de inclusão para o contato: {}", domain.getNome());

        if(repository.existsByEmail(domain.getEmail())){
            throw new IllegalArgumentException("Email já existente: " + domain.getEmail());
        }
        var contato = repository.save(domain);
        log.info("Contato com ID = {} salvo com sucesso!", contato.getId());
        return contato;
    }

}
