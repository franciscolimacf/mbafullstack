package com.agenda.dtos;

import lombok.Data;

@Data
public class ContatoRequest {
    private String nome;
    private String tel;
    private String email;
    private String end;
    private int idade;
    private String tipo;
    private String dataCad;
}
