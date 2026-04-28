package com.agenda.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Contato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String telefone;
    private String email;
    @Column(name = "endereco")
    private String endereco;
    private int idade;
    private String tipo;
    private LocalDateTime dataCad;
    private boolean ativo;


}