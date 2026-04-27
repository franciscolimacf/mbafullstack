package com.agenda.converters;

import com.agenda.domain.ContatoDomain;
import com.agenda.domain.ContatoTipo;
import com.agenda.dtos.ContatoRequest;
import com.agenda.dtos.ContatoResponse;
import com.agenda.entity.Contato;
import com.agenda.entity.ContatoEntity;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class Converter {
    public ContatoDomain ConvertRequestToDomain(ContatoRequest request){
        return ContatoDomain.builder()
                .nome(request.getNome())
                .telefone(request.getTel())
                .endereco(request.getEndereco())
                .idade(request.getIdade())
                .email(request.getEmail())
                .tipo(request.getTipo())
                .build();

    }

    public ContatoEntity ConvertDomainToEntity(ContatoDomain domain){
        var contato = ContatoEntity.builder()
                .nome(domain.getNome())
                .telefone(domain.getTelefone())
                .endereco(domain.getEndereco())
                .idade(domain.getIdade())
                .email(domain.getEmail())
                .dataCad(domain.getDataCad())
                .tipo(domain.getTipo().toString())
                .build();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        contato.setDataCad(sdf.format(new Date()));

        contato.setAtivo(true);

        return contato;
    }

    public ContatoDomain ConvertEntityToDomain(ContatoEntity entity){
        return ContatoDomain.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .telefone(entity.getTelefone())
                .endereco(entity.getEndereco())
                .idade(entity.getIdade())
                .email(entity.getEmail())
                .dataCad(entity.getDataCad())
                .tipo(ContatoTipo.valueOf(entity.getTipo()))
                .build();
    }

    public ContatoResponse ConvertDomainToResponse(ContatoDomain domain){
        return ContatoResponse.builder()
                .id(domain.getId())
                .nome(domain.getNome())
                .telefone(domain.getTelefone())
                .end(domain.getEndereco())
                .idade(domain.getIdade())
                .email(domain.getEmail())
                .dataCad(domain.getDataCad())
                .tipo(domain.getTipo().toString())
                .build();
    }

    public List<ContatoDomain> ConvertListEntityToListDomain(List<ContatoEntity> entities){
        return entities.stream()
                .map(this::ConvertEntityToDomain).toList();
    }

    public List<ContatoResponse> ConvertListDomainToListResponse(List<ContatoDomain> domains){
        return domains.stream()
                .map(this::ConvertDomainToResponse).toList();
    }
}