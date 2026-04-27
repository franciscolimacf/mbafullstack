package com.agenda.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
public class Contato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String nome;
    public String telefone;
    public String email;
    @Column(name = "endereco")
    public String endereco;
    public int idade;
    public String tipo;
    public String dataCad;
    public String ativo;

    // construtor vazio pro JPA
    public Contato() {
    }

    // construtor com tudo
    public Contato(Long id, String nome, String telefone, String email, String endereco, int idade, String tipo, String dataCad,
            String ativo) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
        this.idade = idade;
        this.tipo = tipo;
        this.dataCad = dataCad;
        this.ativo = ativo;
    }

    // getters e setters - alguns sim outros nao
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTel() {
        return telefone;
    }

    public void setTel(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEnd() {
        return endereco;
    }

    public void setEnd(String endereco) {
        this.endereco = endereco;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDataCad() {
        return dataCad;
    }

    public void setDataCad(String dataCad) {
        this.dataCad = dataCad;
    }

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }

    // metodo que valida o contato - poderia estar em outro lugar mas ta aqui mesmo
    public boolean valida() {
        if (this.nome == null || this.nome.equals(""))
            return false;
        if (this.nome.length() < 3)
            return false;
        if (this.telefone == null || this.telefone.equals(""))
            return false;
        if (this.email == null || this.email.equals(""))
            return false;
        if (!this.email.contains("@"))
            return false;
        if (this.idade < 0 || this.idade > 150)
            return false;
        if (this.tipo == null)
            return false;
        if (!this.tipo.equals("FAMILIA") && !this.tipo.equals("AMIGO") && !this.tipo.equals("TRABALHO")
                && !this.tipo.equals("OUTRO"))
            return false;
        return true;
    }

    // formata o contato pra exibir - mistura model com view
    public String formatar() {
        String s = "";
        s = s + "ID: " + this.id + " | ";
        s = s + "Nome: " + this.nome + " | ";
        s = s + "Tel: " + this.telefone + " | ";
        s = s + "Email: " + this.email + " | ";
        s = s + "End: " + this.endereco + " | ";
        s = s + "Idade: " + this.idade + " | ";
        s = s + "Tipo: " + this.tipo + " | ";
        s = s + "Ativo: " + this.ativo;
        return s;
    }
}
