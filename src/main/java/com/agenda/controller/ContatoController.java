package com.agenda.controller;

import com.agenda.converters.Converter;
import com.agenda.dtos.ContatoRequest;
import com.agenda.dtos.ContatoResponse;
import com.agenda.services.strategies.ContatoStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contatos")
public class ContatoController {

    private final ContatoStrategy strategy;
    private final Converter converter;

    @PostMapping("/incluir")
    public ResponseEntity<String> incluir(@RequestBody ContatoRequest request){

            var domain = strategy.incluir(converter.ConvertRequestToDomain(request));
            var response = converter.ConvertDomainToResponse(domain);
            return ResponseEntity.ok("Usuário cadastrado com id: " + response.getId());
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ContatoResponse>> listar(){
        var domain = strategy.listar();
        return ResponseEntity.ok(converter.ConvertListDomainToListResponse(domain));
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<List<ContatoResponse>> pesquisar (@RequestParam String tipo, @RequestParam String valor) {
        var domain = strategy.pesquisar(tipo, valor);
        return ResponseEntity.ok(converter.ConvertListDomainToListResponse(domain));

    }
    @PutMapping("/editar/{id}")
    public ResponseEntity<ContatoResponse> editar(@PathVariable Long id, @RequestBody ContatoRequest request) {

        var domain = strategy.editar(id, converter.ConvertRequestToDomain(request));
        return ResponseEntity.ok(converter.ConvertDomainToResponse(domain));



    }


    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        strategy.excluir(id);
        return ResponseEntity.noContent().build();

    }


}
