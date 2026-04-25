package com.agenda.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ContatoDomain {
    private Long id;
    private String nome;
    private String tel;
    private String email;
    private String end;
    private int idade;
    private String tipo;
    private String dataCad;
}
