package com.agenda.services;

import com.agenda.converters.Converter;
import com.agenda.domain.ContatoDomain;
import com.agenda.entity.Contato;
import com.agenda.repository.ContatoRepository;
import com.sun.jdi.request.DuplicateRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ContatoService {

    private ContatoRepository repository;
    private Converter converter;

    public ContatoService(ContatoRepository repository, Converter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    public ContatoDomain incluir (ContatoDomain domain){

        repository.VerificarEmail(domain.getEmail());
        var contato = repository.save(converter.ConvertDomainToEntity(domain));
        return converter.ConvertEntityToDomain(contato);
    }
}
