package com.agenda.app.adapters.input;

import com.agenda.app.adapters.converters.Converter;
import com.agenda.app.adapters.input.dtos.ContatoRequest;
import com.agenda.app.application.port.in.IContatoServicePort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contatos")
public class ContatoController {

    private final IContatoServicePort servicePort;
    private final Converter converter;

    @PostMapping("/incluir")
    public ResponseEntity<String> incluir(@Valid @RequestBody ContatoRequest request){
            var domain = servicePort.incluir(converter.ConvertRequestToDomain(request));
            var response = converter.ConvertDomainToResponse(domain);
            return ResponseEntity.ok("Usuário cadastrado com id: " + response.getId());
    }

}
