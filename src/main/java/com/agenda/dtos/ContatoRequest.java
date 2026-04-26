package com.agenda.dtos;

import com.agenda.domain.ContatoTipo;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ContatoRequest {
    @NotBlank(message = "O nome é obrigatório.")
    private String nome;
    @NotBlank(message = "O telefone é obrigatório")
    private String tel;
    @Email(message = "O e-mail deve ser válido")
    private String email;
    @NotBlank(message = "O endereço é obrigatório")
    private String endereco;
    @Min(value = 0, message = "A idade não pode ser negativa.")
    @Max(value = 150, message = "Idade inválida.")
    private int idade;
    @NotNull(message = "O tipo é obrigatório.")
    private ContatoTipo tipo;
}
