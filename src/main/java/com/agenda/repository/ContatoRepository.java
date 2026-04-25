package com.agenda.repository;

import com.agenda.domain.ContatoDomain;
import com.agenda.entity.Contato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContatoRepository extends JpaRepository<Contato, Long> {

    public default boolean VerificarEmail(String email){
        List<Contato> todos = this.findAll();

        for (int i = 0; i < todos.size(); i++) {
            if (todos.get(i).getEmail() != null && todos.get(i).getEmail().equals(email)) {
                return false;
            }
        }
        return true;
    }

}
