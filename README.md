
# 📒 Agenda de Contatos
 
API REST para gerenciamento de contatos, desenvolvida com Java e Spring Boot para uso em sala de aula.
 
## Tecnologias
 
Java 17 · Spring Boot · Spring Data JPA · Spring Validation · Lombok · PostgreSQL
 
## Como rodar
 
```properties
# application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/agenda
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
```
 
```bash
./mvnw spring-boot:run
```
 
## Endpoints
 
| método | rota | descrição |
|--------|------|-----------|
| `POST` | `/contatos/incluir` | cadastra contato |
| `GET` | `/contatos/listar` | lista todos |
| `GET` | `/contatos/pesquisar?tipo=&valor=` | busca por nome, email, telefone ou tipo |
| `PUT` | `/contatos/editar/{id}` | edita contato |
| `DELETE` | `/contatos/excluir/{id}` | remove contato |
 
### Exemplo de request
 
```json
{
  "nome": "João Silva",
  "telefone": "11999999999",
  "email": "joao@email.com",
  "endereco": "Rua das Flores, 123",
  "idade": 28,
  "tipo": "AMIGO"
}
```
 
Tipos disponíveis: `AMIGO` `FAMILIA` `TRABALHO` `OUTRO`
 
## Erros
 
```json
{
  "status": 404,
  "mensagem": "Contato não encontrado com id: 1",
  "timestamp": "2026-04-29T10:30:00"
}
```
 
---
 
Feito por **Carlos Gabriel** e **Francisco Lima**
