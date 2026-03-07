# 💸 FluxPay

API REST de transferências financeiras entre usuários, desenvolvida com Java 21 e Spring Boot 3.

## 📋 Sobre o projeto

O FluxPay simula um sistema de pagamentos simplificado, onde usuários podem transferir dinheiro entre si respeitando regras de negócio reais — como validação de saldo, tipo de usuário e autorização externa.

O projeto foi desenvolvido com foco em boas práticas de desenvolvimento backend, incluindo arquitetura em camadas, tratamento de exceções, migrations de banco de dados e testes automatizados.

## 🚀 Tecnologias

- Java 21
- Spring Boot 3.5
- Spring Data JPA
- PostgreSQL
- Flyway Migration
- Lombok
- Docker & Docker Compose
- JUnit 5 + Mockito

## ⚙️ Como rodar localmente

### Pré-requisitos

- Java 21+
- Docker Desktop

### Passo a passo

1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/fluxpay.git
cd fluxpay
```

2. Suba o banco de dados com Docker

```bash
docker compose up -d
```

3. Execute a aplicação

```bash
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

## 📦 Endpoints

### Usuários

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/users` | Cadastra um novo usuário |

**Body da requisição:**

```json
{
  "name": "João Silva",
  "document": "12345678901",
  "email": "joao@email.com",
  "password": "senha123",
  "userType": "COMMON"
}
```

**Response (`201 Created`):**

```json
{
  "id": "uuid-gerado",
  "name": "João Silva",
  "email": "joao@email.com",
  "userType": "COMMON",
  "balance": 0
}
```

### Transferências

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/transactions` | Realiza uma transferência entre usuários |

**Body da requisição:**

```json
{
  "senderId": "uuid-do-remetente",
  "receiverId": "uuid-do-destinatário",
  "amount": 100.00
}
```

**Response (`201 Created`):**

```json
{
  "transactionID": "uuid-da-transacao",
  "amount": 100.00,
  "sender": {
    "id": "uuid-do-remetente",
    "name": "João Silva",
    "userType": "COMMON"
  },
  "receiver": {
    "id": "uuid-do-destinatario",
    "name": "Loja ABC",
    "userType": "MERCHANT"
  },
  "createdAt": "2026-03-06T21:14:40"
}
```

## 🗄️ Banco de dados

```
users
├── user_id (UUID, PK)
├── name (VARCHAR)
├── document (VARCHAR, UNIQUE) — CPF ou CNPJ
├── email (VARCHAR, UNIQUE)
├── password (VARCHAR) — hash bcrypt
├── balance (DECIMAL)
└── user_type (VARCHAR) — COMMON ou MERCHANT

transactions
├── transaction_id (UUID, PK)
├── amount (DECIMAL)
├── sender_id (UUID, FK → users)
├── receiver_id (UUID, FK → users)
└── created_at (TIMESTAMP)
```

## 📐 Regras de negócio

- Apenas usuários do tipo `COMMON` podem enviar transferências
- Usuários do tipo `MERCHANT` apenas recebem
- O remetente deve ter saldo suficiente para a transferência
- Toda transferência é validada por um serviço externo de autorização
- Em caso de falha, a transação é revertida por completo (`@Transactional`)
- Uma notificação é disparada ao recebedor após a transferência

## 🧪 Testes

O projeto conta com testes unitários cobrindo todos os cenários de negócio do `TransactionService`:

- Transferência realizada com sucesso
- Sender não encontrado
- Sender do tipo MERCHANT tentando enviar
- Saldo insuficiente
- Autorização negada pelo serviço externo

Para rodar os testes:

```bash
./mvnw test
```

## 🏗️ Decisões técnicas

**`@Transactional` no serviço de transferência**
Toda a operação de transferência — débito, crédito e persistência — acontece dentro de uma única transação. Se qualquer etapa falhar, o banco retorna ao estado anterior automaticamente, evitando inconsistências.

**`FetchType.LAZY` nos relacionamentos**
Os objetos `User` vinculados a uma `Transaction` são carregados do banco apenas quando necessário, evitando queries desnecessárias ao listar transações.

**Flyway para migrations**
O versionamento do banco é feito via Flyway, garantindo que qualquer desenvolvedor que clonar o projeto tenha exatamente o mesmo schema, sem depender do `ddl-auto: update` do Hibernate.

**DTOs para entrada e saída de dados**
As entidades JPA nunca são expostas diretamente na API. O uso de DTOs garante controle sobre o que entra e sai, protegendo dados sensíveis como senha e saldo, além de desacoplar o contrato da API do modelo de banco de dados.

**`BigDecimal` para valores monetários**
Tipos como `Double` e `Float` têm problemas de arredondamento em ponto flutuante. `BigDecimal` garante precisão total para operações financeiras.

**Desabilitando o `DefaultResponseErrorHandler` no `RestTemplate`**
Por padrão, o `RestTemplate` do Spring lança uma exceção para qualquer resposta HTTP com status 4xx ou 5xx, antes mesmo de você conseguir ler o body da resposta. O serviço externo de autorização retorna 403 quando uma transação é negada — um comportamento esperado, não um erro real. Para tratar esse cenário corretamente, o `errorHandler` foi desabilitado globalmente no `AppConfig`, permitindo que o `AuthorizationService` leia o body e decida como reagir. O trade-off dessa abordagem é que outras chamadas HTTP feitas pelo mesmo `RestTemplate` também não vão lançar exceção em caso de erro, exigindo verificação manual do status em cada integração.

**Validação de entrada com `@Valid` e `jakarta.validation`**
Os campos dos DTOs de request são validados automaticamente pelo Spring antes de chegarem nos controllers, usando anotações como `@NotBlank`, `@NotNull` e `@Positive`. Isso evita que dados inválidos cheguem à camada de serviço.

## Comandos e Queries Relevantes Para Teste

**Rodar o Postgres pelo terminal**
```bash
docker exec -it postgres_local psql -U user_dev -d mydatabase
```

**Inserir usuários de teste (exemplo)**
```sql
INSERT INTO users (user_id, name, document, email, password, balance, user_type) 
VALUES (gen_random_uuid(), 'João Silva', '12345678901', 'joao@email.com', '123456', 1000.00, 'COMMON');

INSERT INTO users (user_id, name, document, email, password, balance, user_type) 
VALUES (gen_random_uuid(), 'Loja ABC', '12345678000195', 'loja@email.com', '123456', 0.00, 'MERCHANT');
```

**Consulta para confirmar a inserção**
```sql
SELECT user_id, name, user_type, balance FROM users;
```