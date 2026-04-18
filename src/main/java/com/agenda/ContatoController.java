package com.agenda;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/contatos")
public class ContatoController {

    @Autowired
    public ContatoRepository repo;

    public static List<String> logs = new ArrayList<>();

    public static int cont = 0;

    public static boolean init = false;

    @PostMapping("/incluir")
    public ResponseEntity<String> incluir(@RequestBody Contato c) {
        try {
            if (c.nome == null || c.nome.equals("")) {
                logs.add("erro: nome vazio - " + new Date());
                return ResponseEntity.badRequest().body("erro: nome obrigatorio");
            }
            if (c.nome.length() < 3) {
                logs.add("erro: nome curto - " + new Date());
                return ResponseEntity.badRequest().body("erro: nome muito curto");
            }
            if (c.tel == null || c.tel.equals("")) {
                logs.add("erro: tel vazio - " + new Date());
                return ResponseEntity.badRequest().body("erro: telefone obrigatorio");
            }
            if (c.email == null || c.email.equals("")) {
                logs.add("erro: email vazio - " + new Date());
                return ResponseEntity.badRequest().body("erro: email obrigatorio");
            }
            if (!c.email.contains("@")) {
                logs.add("erro: email invalido - " + new Date());
                return ResponseEntity.badRequest().body("erro: email invalido");
            }
            if (!c.email.contains(".")) {
                logs.add("erro: email invalido - " + new Date());
                return ResponseEntity.badRequest().body("erro: email invalido");
            }
            if (c.idade < 0) {
                return ResponseEntity.badRequest().body("erro: idade invalida");
            }
            if (c.idade > 150) {
                return ResponseEntity.badRequest().body("erro: idade invalida");
            }
            if (c.tipo == null) {
                return ResponseEntity.badRequest().body("erro: tipo obrigatorio");
            }
            if (!c.tipo.equals("FAMILIA") && !c.tipo.equals("AMIGO") && !c.tipo.equals("TRABALHO")
                    && !c.tipo.equals("OUTRO")) {
                return ResponseEntity.badRequest().body("erro: tipo invalido. Use: FAMILIA, AMIGO, TRABALHO ou OUTRO");
            }

            List<Contato> todos = repo.findAll();
            for (int i = 0; i < todos.size(); i++) {
                if (todos.get(i).email != null && todos.get(i).email.equals(c.email)) {
                    return ResponseEntity.badRequest().body("erro: ja existe contato com esse email");
                }
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            c.dataCad = sdf.format(new Date());

            if (c.ativo == null || c.ativo.equals("")) {
                c.ativo = "S";
            }

            Contato salvo = repo.save(c);

            cont = cont + 1;

            logs.add("contato incluido: " + salvo.id + " - " + salvo.nome + " em " + new Date());

            System.out.println("=== EMAIL ENVIADO ===");
            System.out.println("Para: " + salvo.email);
            System.out.println("Assunto: Bem vindo a agenda");
            System.out.println("Ola " + salvo.nome + ", voce foi cadastrado na agenda!");
            System.out.println("=====================");

            String resp = "Contato incluido com sucesso!\n";
            resp = resp + "ID: " + salvo.id + "\n";
            resp = resp + "Nome: " + salvo.nome + "\n";
            resp = resp + "Tel: " + salvo.tel + "\n";
            resp = resp + "Email: " + salvo.email + "\n";
            resp = resp + "Tipo: " + salvo.tipo + "\n";
            resp = resp + "Cadastrado em: " + salvo.dataCad;

            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            logs.add("erro inesperado ao incluir: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("erro interno: " + e.getMessage());
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<String> listar() {
        try {
            List<Contato> lista = repo.findAll();
            if (lista == null || lista.size() == 0) {
                return ResponseEntity.ok("nenhum contato cadastrado");
            }
            String s = "=== LISTA DE CONTATOS ===\n";
            s = s + "Total: " + lista.size() + "\n\n";
            for (int i = 0; i < lista.size(); i++) {
                Contato ct = lista.get(i);
                s = s + "---------------------------\n";
                s = s + "ID: " + ct.id + "\n";
                s = s + "Nome: " + ct.nome + "\n";
                s = s + "Tel: " + ct.tel + "\n";
                s = s + "Email: " + ct.email + "\n";
                s = s + "End: " + ct.end + "\n";
                s = s + "Idade: " + ct.idade + "\n";
                s = s + "Tipo: " + ct.tipo + "\n";
                s = s + "Cadastro: " + ct.dataCad + "\n";
                s = s + "Ativo: " + ct.ativo + "\n";
            }
            logs.add("listou contatos em " + new Date());
            return ResponseEntity.ok(s);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("erro interno: " + e.getMessage());
        }
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<String> pesquisar(@RequestParam String tipoBusca, @RequestParam String valor) {
        try {
            List<Contato> todos = repo.findAll();
            List<Contato> achados = new ArrayList<>();

            if (tipoBusca.equals("nome")) {
                for (int i = 0; i < todos.size(); i++) {
                    if (todos.get(i).nome != null && todos.get(i).nome.toLowerCase().contains(valor.toLowerCase())) {
                        achados.add(todos.get(i));
                    }
                }
            } else if (tipoBusca.equals("email")) {
                for (int i = 0; i < todos.size(); i++) {
                    if (todos.get(i).email != null && todos.get(i).email.toLowerCase().contains(valor.toLowerCase())) {
                        achados.add(todos.get(i));
                    }
                }
            } else if (tipoBusca.equals("tel")) {
                for (int i = 0; i < todos.size(); i++) {
                    if (todos.get(i).tel != null && todos.get(i).tel.contains(valor)) {
                        achados.add(todos.get(i));
                    }
                }
            } else if (tipoBusca.equals("tipo")) {
                for (int i = 0; i < todos.size(); i++) {
                    if (todos.get(i).tipo != null && todos.get(i).tipo.equals(valor)) {
                        achados.add(todos.get(i));
                    }
                }
            } else if (tipoBusca.equals("id")) {
                try {
                    Long idBusca = Long.parseLong(valor);
                    for (int i = 0; i < todos.size(); i++) {
                        if (todos.get(i).id.equals(idBusca)) {
                            achados.add(todos.get(i));
                        }
                    }
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body("erro: id invalido");
                }
            } else {
                return ResponseEntity.badRequest()
                        .body("erro: tipo de busca invalido. Use: nome, email, tel, tipo ou id");
            }

            if (achados.size() == 0) {
                return ResponseEntity.ok("nenhum contato encontrado");
            }

            String s = "=== RESULTADO DA PESQUISA ===\n";
            s = s + "Encontrados: " + achados.size() + "\n\n";
            for (int i = 0; i < achados.size(); i++) {
                Contato ct = achados.get(i);
                s = s + "---------------------------\n";
                s = s + "ID: " + ct.id + "\n";
                s = s + "Nome: " + ct.nome + "\n";
                s = s + "Tel: " + ct.tel + "\n";
                s = s + "Email: " + ct.email + "\n";
                s = s + "End: " + ct.end + "\n";
                s = s + "Idade: " + ct.idade + "\n";
                s = s + "Tipo: " + ct.tipo + "\n";
                s = s + "Cadastro: " + ct.dataCad + "\n";
                s = s + "Ativo: " + ct.ativo + "\n";
            }

            logs.add("pesquisou por " + tipoBusca + " valor=" + valor);
            return ResponseEntity.ok(s);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("erro interno: " + e.getMessage());
        }
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<String> editar(@PathVariable Long id, @RequestBody Contato c) {
        try {
            Optional<Contato> op = repo.findById(id);
            if (!op.isPresent()) {
                return ResponseEntity.status(404).body("contato nao encontrado");
            }
            Contato atual = op.get();

            if (c.nome != null && !c.nome.equals("")) {
                if (c.nome.length() < 3) {
                    return ResponseEntity.badRequest().body("erro: nome muito curto");
                }
                atual.nome = c.nome;
            }
            if (c.tel != null && !c.tel.equals("")) {
                atual.tel = c.tel;
            }
            if (c.email != null && !c.email.equals("")) {
                if (!c.email.contains("@")) {
                    return ResponseEntity.badRequest().body("erro: email invalido");
                }
                if (!c.email.contains(".")) {
                    return ResponseEntity.badRequest().body("erro: email invalido");
                }
                // verifica duplicado
                List<Contato> todos = repo.findAll();
                for (int i = 0; i < todos.size(); i++) {
                    if (todos.get(i).email != null && todos.get(i).email.equals(c.email)
                            && !todos.get(i).id.equals(id)) {
                        return ResponseEntity.badRequest().body("erro: ja existe contato com esse email");
                    }
                }
                atual.email = c.email;
            }
            if (c.end != null && !c.end.equals("")) {
                atual.end = c.end;
            }
            if (c.idade > 0) {
                if (c.idade > 150) {
                    return ResponseEntity.badRequest().body("erro: idade invalida");
                }
                atual.idade = c.idade;
            }
            if (c.tipo != null && !c.tipo.equals("")) {
                if (!c.tipo.equals("FAMILIA") && !c.tipo.equals("AMIGO") && !c.tipo.equals("TRABALHO")
                        && !c.tipo.equals("OUTRO")) {
                    return ResponseEntity.badRequest().body("erro: tipo invalido");
                }
                atual.tipo = c.tipo;
            }
            if (c.ativo != null && !c.ativo.equals("")) {
                atual.ativo = c.ativo;
            }

            Contato salvo = repo.save(atual);

            logs.add("contato editado: " + salvo.id + " em " + new Date());

            String resp = "Contato editado com sucesso!\n";
            resp = resp + "ID: " + salvo.id + "\n";
            resp = resp + "Nome: " + salvo.nome + "\n";
            resp = resp + "Tel: " + salvo.tel + "\n";
            resp = resp + "Email: " + salvo.email + "\n";
            resp = resp + "End: " + salvo.end + "\n";
            resp = resp + "Idade: " + salvo.idade + "\n";
            resp = resp + "Tipo: " + salvo.tipo + "\n";
            resp = resp + "Ativo: " + salvo.ativo;

            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("erro interno: " + e.getMessage());
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<String> excluir(@PathVariable Long id) {
        try {
            Optional<Contato> op = repo.findById(id);
            if (!op.isPresent()) {
                return ResponseEntity.status(404).body("contato nao encontrado");
            }
            Contato c = op.get();

            if (c.tipo != null && c.tipo.equals("FAMILIA")) {
                return ResponseEntity.badRequest().body("erro: nao pode excluir contato do tipo FAMILIA");
            }

            repo.deleteById(id);

            logs.add("contato excluido: " + id + " - " + c.nome + " em " + new Date());

            System.out.println("=== EMAIL ENVIADO ===");
            System.out.println("Para: " + c.email);
            System.out.println("Assunto: Contato removido");
            System.out.println("Ola " + c.nome + ", voce foi removido da agenda!");
            System.out.println("=====================");

            return ResponseEntity.ok("contato " + id + " excluido com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("erro interno: " + e.getMessage());
        }
    }

    @GetMapping("/logs")
    public ResponseEntity<String> verLogs() {
        String s = "=== LOGS ===\n";
        s = s + "Total operacoes: " + cont + "\n\n";
        for (int i = 0; i < logs.size(); i++) {
            s = s + logs.get(i) + "\n";
        }
        return ResponseEntity.ok(s);
    }
}
