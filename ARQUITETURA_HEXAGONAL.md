# Operadora Cartão de Crédito - Arquitetura Hexagonal

## 📋 Resumo das Melhorias Aplicadas

Este documento descreve as melhorias aplicadas no projeto para seguir corretamente a **Arquitetura Hexagonal (Ports and Adapters)**.

---

## ✅ Mudanças Implementadas

### 1. **Correção de Bugs Críticos**

#### CartaoDomain
- ✅ Adicionado construtor vazio para resolver erro do MapStruct
- ✅ Corrigido segundo construtor que não atribuía valores corretamente

#### GeraDadosCartaoService
- ✅ Corrigido NullPointerException ao tentar acessar `cliente.getCartao()`
- ✅ Agora cria corretamente o cartão com valores inicializados

---

### 2. **Reorganização da Estrutura (Arquitetura Hexagonal)**

#### Nova Estrutura de Diretórios

```
application/
├── domain/                          → Entidades de domínio puras
│   ├── CartaoDomain.java
│   ├── ClienteDomain.java
│   ├── EnderecoDomain.java
│   └── exceptions/                  → Exceções de negócio
│       ├── ClienteJaPossuiCartaoException.java
│       └── ClienteNaoEncontradoException.java
├── ports/                           → Portas (interfaces/contratos)
│   ├── in/                          → Portas de ENTRADA (Use Cases)
│   │   ├── SolicitarCartaoUseCase.java
│   │   └── BuscarClientePorCpfUseCase.java
│   └── out/                         → Portas de SAÍDA
│       └── ClienteRepository.java
└── services/                        → Implementação dos Use Cases
    ├── IClienteServiceImpl.java
    └── GeraDadosCartaoService.java

adapters/
├── in/                              → Adaptadores de ENTRADA
│   ├── controller/                  → Controllers REST
│   │   └── ClienteController.java
│   ├── dto/                         → DTOs de entrada/saída
│   │   ├── request/
│   │   │   ├── ClienteRequestDTO.java
│   │   │   └── EnderecoRequestDTO.java
│   │   └── response/
│   │       ├── ClienteResponseDTO.java
│   │       └── CartaoResponseDTO.java
│   └── handler/                     → Tratamento de exceções
│       └── GlobalExceptionHandler.java
├── out/                             → Adaptadores de SAÍDA
│   ├── ClienteRepositoryImpl.java   → Implementação JPA
│   ├── entities/                    → Entidades JPA
│   │   ├── CartaoEntity.java
│   │   ├── ClienteEntity.java
│   │   └── EnderecoEntity.java
│   └── repositories/                → Spring Data JPA
│       └── IClienteJpaRepository.java
└── mapper/
    └── ClienteMapper.java           → MapStruct
```

---

### 3. **Criação de Use Cases (Portas de Entrada)**

Substituímos a interface genérica `IClientService` por use cases específicos:

- **`SolicitarCartaoUseCase`**: responsável por solicitar cartão
- **`BuscarClientePorCpfUseCase`**: responsável por buscar cliente

**Benefícios:**
- Cada use case tem uma única responsabilidade (SRP)
- Facilita testes unitários
- Deixa clara a intenção do código

---

### 4. **Criação de Porta de Saída**

Movemos `IClienteRepository` de `porters/saida/` para `application/ports/out/ClienteRepository`:

- Removido prefixo "I" (não é necessário em Java moderno)
- Renomeado métodos para serem mais claros:
  - `buscaUsuarioPorEmail` → `existePorEmail`
  - `buscarUsuarioPorCpf` → `buscarPorCpf` (mantido)

---

### 5. **Exceções Customizadas de Domínio**

Criadas exceções específicas do domínio:

- **`ClienteJaPossuiCartaoException`**: quando cliente já tem cartão
- **`ClienteNaoEncontradoException`**: quando cliente não é encontrado

**Benefícios:**
- Exceções específicas que representam regras de negócio
- Facilitam tratamento específico no handler
- Melhor mensagem de erro para o usuário

---

### 6. **Handler Global de Exceções**

Criado `GlobalExceptionHandler` com tratamento para:

- **409 Conflict**: Cliente já possui cartão
- **404 Not Found**: Cliente não encontrado
- **400 Bad Request**: Erros de validação
- **500 Internal Server Error**: Erros genéricos

**Formato de resposta de erro:**
```json
{
  "status": 409,
  "message": "Cliente com email joao@email.com já possui um cartão.",
  "timestamp": "2026-05-13T10:30:00"
}
```

**Formato de resposta de validação:**
```json
{
  "status": 400,
  "message": "Erro de validação",
  "timestamp": "2026-05-13T10:30:00",
  "errors": {
    "email": "Email deve ser válido",
    "idade": "Idade mínima é 18 anos"
  }
}
```

---

### 7. **Validações nos DTOs**

Adicionadas validações Bean Validation (Jakarta Validation):

#### ClienteRequestDTO
- `nome`: obrigatório, 3-100 caracteres
- `email`: obrigatório, formato válido
- `idade`: obrigatória, 18-120 anos
- `cpf`: obrigatório, formato XXX.XXX.XXX-XX ou 11 dígitos
- `rendaMensal`: obrigatória, valor positivo
- `endereco`: obrigatório, validado recursivamente

#### EnderecoRequestDTO
- `rua`: obrigatória, 3-200 caracteres
- `numero`: obrigatório, positivo
- `cidade`: obrigatória, 2-100 caracteres
- `estado`: obrigatório, 2 caracteres (sigla)
- `cep`: obrigatório, formato XXXXX-XXX ou 8 dígitos

---

### 8. **Atualização do Controller**

O `ClienteController` agora:

- Usa diretamente os use cases (`SolicitarCartaoUseCase`, `BuscarClientePorCpfUseCase`)
- Retorna status HTTP corretos (201 Created, 200 OK)
- Valida entrada com `@Valid`
- Não contém lógica de negócio (apenas coordenação)

---

## 🚀 Como Testar

### 1. Compilar o projeto
```powershell
.\gradlew clean build
```

### 2. Executar a aplicação
```powershell
.\gradlew bootRun
```

### 3. Testar endpoint POST /cliente

**Request de SUCESSO:**
```bash
curl -X POST http://localhost:8080/cliente \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João da Silva",
    "email": "joao@email.com",
    "idade": 30,
    "cpf": "123.456.789-10",
    "rendaMensal": 5000.00,
    "dataVencimentoFatura": "10",
    "endereco": {
      "rua": "Rua das Flores",
      "numero": 123,
      "complemento": "Apto 101",
      "cidade": "São Paulo",
      "estado": "SP",
      "cep": "01234-567"
    }
  }'
```

**Request com ERRO de validação:**
```bash
curl -X POST http://localhost:8080/cliente \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Jo",
    "email": "email-invalido",
    "idade": 15,
    "cpf": "123",
    "rendaMensal": -1000,
    "endereco": {}
  }'
```

### 4. Testar endpoint GET /cliente

```bash
curl -X GET "http://localhost:8080/cliente?cpf=123.456.789-10"
```

---

## 📚 Conceitos de Arquitetura Hexagonal Aplicados

### Camada de Domínio (Centro do Hexágono)
- **Onde**: `application/domain/`
- **O que**: Entidades de domínio puras, sem dependências externas
- **Regra**: NÃO pode depender de frameworks (Spring, JPA, etc.)

### Portas (Interfaces)
- **Onde**: `application/ports/in/` e `application/ports/out/`
- **O que**: Contratos que definem como interagir com o domínio
- **Tipos**:
  - **Portas de ENTRADA (in)**: Use Cases que a aplicação oferece
  - **Portas de SAÍDA (out)**: Dependências que a aplicação precisa

### Adaptadores
- **Onde**: `adapters/in/` e `adapters/out/`
- **O que**: Implementações concretas que conectam o mundo externo ao domínio
- **Tipos**:
  - **Adaptadores de ENTRADA**: Controllers REST, Listeners, CLI, etc.
  - **Adaptadores de SAÍDA**: Repositórios JPA, Clients HTTP, Filas, etc.

### Fluxo de Dados

```
┌─────────────────────────────────────────────────┐
│         Mundo Externo (Cliente HTTP)            │
└─────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────┐
│   ADAPTADOR DE ENTRADA (ClienteController)      │
│   - Recebe ClienteRequestDTO                    │
│   - Converte DTO → Domain                       │
└─────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────┐
│   PORTA DE ENTRADA (SolicitarCartaoUseCase)     │
│   - Interface que define o contrato             │
└─────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────┐
│   NÚCLEO (IClienteServiceImpl)                  │
│   - Executa regras de negócio                   │
│   - Valida email duplicado                      │
│   - Gera cartão                                 │
└─────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────┐
│   PORTA DE SAÍDA (ClienteRepository)            │
│   - Interface que define o contrato             │
└─────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────┐
│   ADAPTADOR DE SAÍDA (ClienteRepositoryImpl)    │
│   - Converte Domain → Entity                    │
│   - Usa JPA para persistir                      │
└─────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────┐
│         Mundo Externo (Banco de Dados)          │
└─────────────────────────────────────────────────┘
```

---

## 🎯 Benefícios Alcançados

1. **Testabilidade**: Núcleo pode ser testado sem subir Spring/JPA
2. **Independência de Framework**: Domínio não conhece Spring
3. **Flexibilidade**: Fácil trocar JPA por MongoDB, por exemplo
4. **Clareza**: Cada camada tem responsabilidade bem definida
5. **Manutenibilidade**: Código organizado e fácil de entender

---

## 📖 Próximos Passos Sugeridos

1. Adicionar testes unitários para o domínio
2. Adicionar testes de integração
3. Implementar outros use cases (atualizar limite, consultar fatura, etc.)
4. Adicionar documentação Swagger/OpenAPI
5. Implementar paginação na busca de clientes
6. Adicionar logs estruturados

---

## 🤝 Como Contribuir

1. Sempre crie use cases específicos (uma interface por responsabilidade)
2. Mantenha o domínio livre de dependências externas
3. Use exceções customizadas para representar regras de negócio
4. Adicione validações nos DTOs de entrada
5. Documente novos endpoints no README

---

**Autor das melhorias**: GitHub Copilot  
**Data**: 2026-05-13

