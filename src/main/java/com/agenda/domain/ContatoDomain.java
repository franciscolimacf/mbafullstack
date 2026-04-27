package com.agenda.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContatoDomain {
    private Long id;
    private String nome;
    private String telefone;
    private String email;
    private String endereco;
    private int idade;
    private ContatoTipo tipo;
    private String dataCad;
}
