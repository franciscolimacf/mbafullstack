package com.agenda.app.adapters.output;

import com.agenda.app.application.domain.ContatoTipo;
import com.agenda.app.adapters.output.entity.ContatoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContatoRepository extends JpaRepository<ContatoEntity, Long> {
    boolean existsByEmail(String email);
}
