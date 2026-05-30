package com.agenda.app.application.port.in;

import com.agenda.app.application.domain.ContatoDomain;

public interface IContatoServicePort {
    ContatoDomain incluir(ContatoDomain domain);
}
