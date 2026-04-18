# Agenda de Contatos — Código Didático (PROPOSITALMENTE RUIM)

Aplicação REST em **Spring Boot + Java 21** para gerenciamento de contatos, construída no padrão MVC mas **intencionalmente violando SOLID e Clean Code**. Serve como material didático para exercícios de refatoração.

## Como executar

```bash
mvn spring-boot:run
```

A aplicação sobe em `http://localhost:8080` com banco H2 em memória (console em `/h2`).

## Endpoints

| Método | URL | Descrição |
|--------|------|-----------|
| POST   | `/contatos/incluir` | Inclui um contato (JSON no body) |
| GET    | `/contatos/listar` | Lista todos |
| GET    | `/contatos/pesquisar?tipoBusca=nome&valor=joao` | Pesquisa (tipos: `nome`, `email`, `tel`, `tipo`, `id`) |
| PUT    | `/contatos/editar/{id}` | Edita um contato |
| DELETE | `/contatos/excluir/{id}` | Exclui um contato |
| GET    | `/contatos/logs` | Mostra o "log" interno |

### Exemplo de JSON para `POST /contatos/incluir`

```json
{
  "nome": "Maria Silva",
  "tel": "11999998888",
  "email": "maria@exemplo.com",
  "end": "Rua A, 100",
  "idade": 30,
  "tipo": "AMIGO"
}
```

Tipos permitidos: `FAMILIA`, `AMIGO`, `TRABALHO`, `OUTRO`.

---

## Problemas propositais (para o exercício de refatoração)

### Violações de SOLID

- **SRP (Single Responsibility)** — [ContatoController.java](src/main/java/com/agenda/ContatoController.java) faz validação, acesso a dados, regras de negócio, formatação da resposta, logging e "envio de email". [Contato.java](src/main/java/com/agenda/Contato.java) é entidade + validador + formatador.
- **OCP (Open/Closed)** — o método `pesquisar` usa uma cadeia `if/else` por tipo de busca; para adicionar um novo critério é preciso editar o método.
- **LSP (Liskov)** — não há hierarquia; ponto a introduzir em uma etapa posterior (ex.: tipos de contato polimórficos).
- **ISP (Interface Segregation)** — nenhuma interface de serviço — tudo é classe concreta.
- **DIP (Dependency Inversion)** — o controller depende diretamente do repositório concreto; não há camada de serviço nem abstrações.

### Violações de Clean Code

- Nomes ruins e abreviados: `c`, `ct`, `tel`, `end`, `cont`, `op`, `s`, `resp`.
- Campos `public` na entidade (sem encapsulamento).
- Mistura de idiomas (português/inglês).
- Métodos gigantes com muitas responsabilidades (`incluir` tem validação, persistência, email, log e formatação).
- Código duplicado (formatação de saída repetida em `listar`, `pesquisar` e `editar`).
- Magic strings espalhadas (`"FAMILIA"`, `"S"`, `"N"`, `"AMIGO"`...).
- Datas armazenadas como `String`.
- Flag `ativo` como `"S"`/`"N"` em vez de `boolean`.
- Estado estático no controller (`logs`, `cont`, `init`).
- `catch (Exception e)` genérico engolindo tudo.
- Respostas como `String` concatenada em vez de DTOs/JSON.
- Regras de negócio no controller (ex.: "não pode excluir FAMILIA").
- Comentários redundantes que explicam o óbvio.
- Sem DTOs — a entidade JPA é exposta diretamente na API.
- Sem tratamento de exceções centralizado (`@ControllerAdvice`).
- Sem validação declarativa (Bean Validation) — tudo com `if`.

---

## Roteiro sugerido de refatoração

1. Separar em camadas: `controller` → `service` → `repository`.
2. Criar DTOs de entrada e saída (`ContatoRequest`, `ContatoResponse`).
3. Adotar Bean Validation (`@NotBlank`, `@Email`, `@Min`, `@Max`) e um `@ControllerAdvice`.
4. Transformar `tipo` em `enum`, `ativo` em `boolean` e `dataCad` em `LocalDateTime`.
5. Encapsular a entidade (campos `private` + construtor/builder).
6. Extrair formatação de saída para a camada de apresentação (ou usar JSON direto).
7. Substituir o `if/else` de `pesquisar` por Strategy ou por Specifications do Spring Data.
8. Extrair regras de negócio (ex.: proibição de excluir FAMILIA) para o serviço.
9. Substituir logs manuais por `Logger` (SLF4J).
10. Escrever testes unitários e de integração por camada.
