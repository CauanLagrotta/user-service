# User Service

Serviço de gerenciamento de usuários do projeto Saloon Platform.

## Visão Geral

O User Service é responsável pelo cadastro, autenticação e gestão de todos os usuários da plataforma (clientes, profissionais e administradores).

## Porta

**5001**

## Funcionalidades

- Cadastro de usuários
- Autenticação e autorização
- Gestão de perfil de usuário
- Validação de dados
- Consulta de usuários

## Endpoints

| Método | Caminho | Descrição |
|--------|---------|-----------|
| POST | `/api/users` | Cria novo usuário |
| GET | `/api/users/{id}` | Busca usuário por ID |
| PUT | `/api/users/{id}` | Atualiza usuário |
| DELETE | `/api/users/{id}` | Remove usuário |
| GET | `/api/users` | Lista todos os usuários |

## Tecnologias

- Spring Boot 3.5.14
- Spring Data JPA
- Spring Validation
- Flyway (migrações)
- MySQL
- Eureka Client
- Lombok
- Java 21

## Banco de Dados

- **Nome:** userdb
- **Porta:** 3301

## Como Rodar

```bash
mvn clean package
java -jar target/user-service-0.0.1-SNAPSHOT.jar
```

## Migrações

O Flyway gerencia as migrações automaticamente na inicialização. Os scripts ficam em `src/main/resources/db/migration/`.
