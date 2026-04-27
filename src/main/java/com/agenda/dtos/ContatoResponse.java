package com.agenda.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContatoResponse {
    private Long id;
    private String nome;
    private String telefone;
    private String email;
    private String end;
    private int idade;
    private String tipo;
    private String dataCad;
}
