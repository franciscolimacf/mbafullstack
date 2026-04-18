package com.agenda;

import jakarta.persistence.*;

@Entity
@Table(name = "contatos")
public class Contato {

    // id do contato
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id; // publico pq e mais facil

    // nome da pessoa
    public String nome;

    // telefone
    public String tel; // abreviado pra economizar

    // email
    public String email;

    public String end; // endereco abreviado

    public int idade;

    public String tipo; // FAMILIA, AMIGO, TRABALHO, OUTRO - string mesmo

    // data de cadastro - salva como string mesmo pq e mais facil
    public String dataCad;

    // flag se ta ativo
    public String ativo; // "S" ou "N"

    // construtor vazio pro JPA
    public Contato() {
    }

    // construtor com tudo
    public Contato(Long id, String nome, String tel, String email, String end, int idade, String tipo, String dataCad,
            String ativo) {
        this.id = id;
        this.nome = nome;
        this.tel = tel;
        this.email = email;
        this.end = end;
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
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
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
        if (this.tel == null || this.tel.equals(""))
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
        s = s + "Tel: " + this.tel + " | ";
        s = s + "Email: " + this.email + " | ";
        s = s + "End: " + this.end + " | ";
        s = s + "Idade: " + this.idade + " | ";
        s = s + "Tipo: " + this.tipo + " | ";
        s = s + "Ativo: " + this.ativo;
        return s;
    }
}
