package com.agenda.converters;

import com.agenda.domain.ContatoDomain;
import com.agenda.dtos.ContatoRequest;
import com.agenda.dtos.ContatoResponse;
import com.agenda.entity.Contato;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Converter {
    public ContatoDomain ConvertRequestToDomain(ContatoRequest request){
        return ContatoDomain.builder()
                .nome(request.getNome())
                .tel(request.getTel())
                .end(request.getEnd())
                .idade(request.getIdade())
                .email(request.getEmail())
                .dataCad(request.getDataCad())
                .tipo(request.getTipo())
                .build();

    }

    public Contato ConvertDomainToEntity(ContatoDomain domain){
        var contato = Contato.builder()
                .nome(domain.getNome())
                .tel(domain.getTel())
                .end(domain.getEnd())
                .idade(domain.getIdade())
                .email(domain.getEmail())
                .dataCad(domain.getDataCad())
                .tipo(domain.getTipo())
                .build();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        contato.setDataCad(sdf.format(new Date()));

        contato.setAtivo("true");

        return contato;
    }

    public ContatoDomain ConvertEntityToDomain(Contato entity){
        return ContatoDomain.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .tel(entity.getTel())
                .end(entity.getEnd())
                .idade(entity.getIdade())
                .email(entity.getEmail())
                .dataCad(entity.getDataCad())
                .tipo(entity.getTipo())
                .build();
    }

    public ContatoResponse ConvertDomainToResponse(ContatoDomain domain){
        return ContatoResponse.builder()
                .id(domain.getId())
                .nome(domain.getNome())
                .tel(domain.getTel())
                .end(domain.getEnd())
                .idade(domain.getIdade())
                .email(domain.getEmail())
                .dataCad(domain.getDataCad())
                .tipo(domain.getTipo())
                .build();
    }
}