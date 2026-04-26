package com.agenda.repository;

import com.agenda.domain.ContatoTipo;
import com.agenda.entity.ContatoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContatoRepository extends JpaRepository<ContatoEntity, Long> {

    List<ContatoEntity> findByNomeContainingIgnoreCase(String nome);
    List<ContatoEntity> findByEmailContainingIgnoreCase(String email);
    List<ContatoEntity> findByTelefoneContaining(String tel);
    List<ContatoEntity> findByTipo(ContatoTipo tipo);

    public default boolean VerificarEmail(String email){
        List<ContatoEntity> todos = this.findAll();

        for (ContatoEntity todo : todos) {
            if (todo.getEmail() != null && todo.getEmail().equals(email)) {
                return false;
            }
        }
        return true;
    }

}
