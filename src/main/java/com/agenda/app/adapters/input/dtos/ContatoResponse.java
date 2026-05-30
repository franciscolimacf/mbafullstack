package com.agenda.app.adapters.input.dtos;

import com.agenda.app.application.domain.ContatoTipo;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ContatoResponse {
    private Long id;
    private String nome;
    private String telefone;
    private String email;
    private String endereco;
    private int idade;
    private ContatoTipo tipo;
    private LocalDateTime dataCad;
}
