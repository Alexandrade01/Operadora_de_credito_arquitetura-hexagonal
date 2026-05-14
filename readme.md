# Documentação - Arquitetura Hexagonal
## Operadora de Cartão de Crédito

---

## 📚 Índice

1. [O que é Arquitetura Hexagonal](#o-que-é-arquitetura-hexagonal)
2. [Conceitos Fundamentais](#conceitos-fundamentais)
3. [Benefícios da Arquitetura Hexagonal](#benefícios-da-arquitetura-hexagonal)
4. [Arquitetura Hexagonal no Projeto](#arquitetura-hexagonal-no-projeto)
5. [Estrutura do Projeto](#estrutura-do-projeto)
6. [Fluxo de Dados](#fluxo-de-dados)
7. [Componentes da Aplicação](#componentes-da-aplicação)
8. [Exemplos Práticos](#exemplos-práticos)
9. [Regras de Negócio](#regras-de-negócio)
10. [Como Testar](#como-testar)

---

## 🏛️ O que é Arquitetura Hexagonal

A **Arquitetura Hexagonal**, também conhecida como **Ports and Adapters** (Portas e Adaptadores), é um padrão arquitetural criado por **Alistair Cockburn** em 2005. O objetivo principal é criar aplicações que sejam:

- **Independentes de frameworks**
- **Testáveis**
- **Independentes da interface de usuário**
- **Independentes do banco de dados**
- **Independentes de qualquer agente externo**

### Por que "Hexagonal"?

O nome "hexagonal" vem da representação visual do padrão, onde o núcleo da aplicação (domínio) está no centro de um hexágono, e os lados representam diferentes portas de entrada e saída.

```
         ┌───────────────────────────────────┐
         │     Adaptadores de Entrada        │
         │  (Controllers, CLI, Listeners)    │
         └───────────────┬───────────────────┘
                         │
         ┌───────────────▼───────────────────┐
         │     Portas de Entrada (in)        │
         │        (Use Cases)                │
         └───────────────┬───────────────────┘
                         │
         ┌───────────────▼───────────────────┐
         │         DOMÍNIO (Core)            │
         │    Regras de Negócio Puras        │
         │   (Entidades e Lógica de          │
         │         Negócio)                  │
         └───────────────┬───────────────────┘
                         │
         ┌───────────────▼───────────────────┐
         │     Portas de Saída (out)         │
         │      (Repositórios, APIs)         │
         └───────────────┬───────────────────┘
                         │
         ┌───────────────▼───────────────────┐
         │     Adaptadores de Saída          │
         │   (JPA, HTTP Client, Cache)       │
         └───────────────────────────────────┘
```

---

## 🧩 Conceitos Fundamentais

### 1. **Domínio (Domain/Core)**

O **domínio** é o coração da aplicação. Contém:

- **Entidades de domínio**: Objetos que representam conceitos do negócio
- **Regras de negócio**: Lógica que define como a aplicação funciona
- **Exceções de domínio**: Erros que representam violações de regras de negócio

**Características importantes:**
- ✅ Não depende de frameworks (Spring, Hibernate, etc.)
- ✅ Não conhece banco de dados, APIs externas ou interfaces
- ✅ É totalmente testável sem dependências externas
- ✅ Contém a lógica mais importante da aplicação

### 2. **Portas (Ports)**

As **portas** são **interfaces** que definem contratos de comunicação. Existem dois tipos:

#### Portas de Entrada (Input Ports / Driving Ports)

- **Definem o que a aplicação oferece** (Use Cases)
- São **implementadas** pelo núcleo da aplicação
- São **chamadas** pelos adaptadores de entrada

**Exemplo:** `SolicitarCartaoUseCase`, `BuscarClientePorCpfUseCase`

#### Portas de Saída (Output Ports / Driven Ports)

- **Definem o que a aplicação precisa** (dependências)
- São **implementadas** pelos adaptadores de saída
- São **chamadas** pelo núcleo da aplicação

**Exemplo:** `ClienteRepository`

### 3. **Adaptadores (Adapters)**

Os **adaptadores** são implementações concretas que conectam o mundo externo ao domínio.

#### Adaptadores de Entrada (Input Adapters)

- **Recebem requisições** do mundo externo
- **Convertem** dados externos para o formato do domínio
- **Chamam** as portas de entrada (Use Cases)

**Exemplos:**
- Controllers REST
- Listeners de mensageria (Kafka, RabbitMQ)
- Interfaces CLI
- Jobs agendados

#### Adaptadores de Saída (Output Adapters)

- **Implementam** as portas de saída
- **Convertem** dados do domínio para o formato externo
- **Comunicam** com sistemas externos (banco de dados, APIs, cache, etc.)

**Exemplos:**
- Repositórios JPA
- Clients HTTP
- Serviços de mensageria
- Sistemas de cache

---

## 🎯 Benefícios da Arquitetura Hexagonal

### 1. **Testabilidade**

O domínio pode ser testado **sem subir frameworks**, banco de dados ou APIs externas. Você pode criar mocks das portas de saída e testar toda a lógica de negócio de forma isolada.

```java
// Teste unitário do domínio sem Spring
@Test
void deveGerarCartaoParaNovoCliente() {
    // Arrange
    ClienteRepository mockRepository = mock(ClienteRepository.class);
    GeraDadosCartaoService geradorCartao = new GeraDadosCartaoService();
    SolicitarCartaoUseCase useCase = new IClienteServiceImpl(mockRepository, geradorCartao);
    
    // Act & Assert
    ClienteDomain resultado = useCase.solicitarCartao(cliente);
    assertNotNull(resultado.getCartao());
}
```

### 2. **Independência de Framework**

O domínio **não conhece Spring**, JPA, ou qualquer outro framework. Isso significa que:

- Você pode trocar de framework sem reescrever a lógica de negócio
- Você não está preso a versões específicas de frameworks
- Atualizações de frameworks não afetam o núcleo da aplicação

### 3. **Flexibilidade para Mudanças**

Trocar a tecnologia de persistência é **simples**:

- Usando JPA hoje? Troque por MongoDB amanhã!
- Basta criar um novo adaptador de saída
- O domínio permanece intocado

```
JPA → MongoDB
ClienteRepositoryJpaImpl → ClienteRepositoryMongoImpl
```

### 4. **Clareza e Organização**

Cada camada tem **responsabilidade bem definida**:

- **Domínio**: regras de negócio
- **Portas**: contratos
- **Adaptadores**: integração com o mundo externo

### 5. **Manutenibilidade**

O código é organizado por **responsabilidade funcional**, não por tipo técnico. Isso torna mais fácil:

- Encontrar onde está determinada funcionalidade
- Adicionar novos casos de uso
- Modificar comportamentos existentes

---

## 🏗️ Arquitetura Hexagonal no Projeto

### Visão Geral

O projeto **Operadora de Cartão de Crédito** implementa a Arquitetura Hexagonal para gerenciar:

- Solicitação de cartões de crédito
- Cadastro de clientes
- Geração automática de dados do cartão
- Validação de regras de negócio

### Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.4.1**
- **Spring Data JPA**
- **H2 Database** (banco em memória)
- **MapStruct** (mapeamento de objetos)
- **Lombok** (redução de boilerplate)
- **Jakarta Validation** (validação de dados)

---

## 📁 Estrutura do Projeto

```
src/main/java/com/example/operadoracartaocredito/
│
├── application/                          # Núcleo da aplicação (domínio)
│   ├── domain/                           # Entidades de domínio puras
│   │   ├── CartaoDomain.java             # Entidade de cartão
│   │   ├── ClienteDomain.java            # Entidade de cliente
│   │   ├── EnderecoDomain.java           # Entidade de endereço
│   │   └── exceptions/                   # Exceções de negócio
│   │       ├── ClienteJaPossuiCartaoException.java
│   │       └── ClienteNaoEncontradoException.java
│   │
│   ├── ports/                            # Portas (interfaces/contratos)
│   │   ├── in/                           # Portas de ENTRADA (Use Cases)
│   │   │   ├── SolicitarCartaoUseCase.java
│   │   │   └── BuscarClientePorCpfUseCase.java
│   │   └── out/                          # Portas de SAÍDA
│   │       └── ClienteRepository.java
│   │
│   └── services/                         # Implementação dos Use Cases
│       ├── IClienteServiceImpl.java      # Implementa os use cases
│       └── GeraDadosCartaoService.java   # Serviço de geração de cartão
│
└── adapters/                             # Adaptadores
    ├── in/                               # Adaptadores de ENTRADA
    │   ├── controller/                   # Controllers REST
    │   │   └── ClienteController.java
    │   ├── dto/                          # DTOs de entrada/saída
    │   │   ├── request/
    │   │   │   ├── ClienteRequestDTO.java
    │   │   │   └── EnderecoRequestDTO.java
    │   │   └── response/
    │   │       ├── ClienteResponseDTO.java
    │   │       └── CartaoResponseDTO.java
    │   └── handler/                      # Tratamento de exceções
    │       └── GlobalExceptionHandler.java
    │
    ├── out/                              # Adaptadores de SAÍDA
    │   ├── ClienteRepositoryImpl.java    # Implementação JPA do repositório
    │   ├── entities/                     # Entidades JPA
    │   │   ├── CartaoEntity.java
    │   │   ├── ClienteEntity.java
    │   │   └── EnderecoEntity.java
    │   └── repositories/                 # Spring Data JPA
    │       └── IClienteJpaRepository.java
    │
    └── mapper/
        └── ClienteMapper.java            # MapStruct para conversões
```

---

## 🔄 Fluxo de Dados

### Fluxo Completo de uma Requisição

```
┌─────────────────────────────────────────────────────────────┐
│  1. Cliente HTTP faz POST /cliente com JSON                 │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│  2. ADAPTADOR DE ENTRADA (ClienteController)                │
│     - Recebe ClienteRequestDTO                              │
│     - Valida dados com @Valid                               │
│     - Converte DTO → ClienteDomain (via Mapper)             │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│  3. PORTA DE ENTRADA (SolicitarCartaoUseCase)               │
│     - Interface que define o contrato                       │
│     - Método: ClienteDomain solicitarCartao(cliente)        │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│  4. NÚCLEO (IClienteServiceImpl)                            │
│     - Valida se email já existe (regra de negócio)          │
│     - Gera dados do cartão (número, CVV, bandeira)          │
│     - Calcula limite baseado na renda                       │
│     - Cria ClienteDomain completo                           │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│  5. PORTA DE SAÍDA (ClienteRepository)                      │
│     - Interface que define contrato com persistência        │
│     - Métodos: salvar(), buscarPorCpf(), existePorEmail()   │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│  6. ADAPTADOR DE SAÍDA (ClienteRepositoryImpl)              │
│     - Converte ClienteDomain → ClienteEntity (via Mapper)   │
│     - Usa IClienteJpaRepository (Spring Data JPA)           │
│     - Persiste no banco H2                                  │
│     - Converte ClienteEntity → ClienteDomain                │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│  7. Retorno ao Controller                                   │
│     - Converte ClienteDomain → ClienteResponseDTO           │
│     - Retorna HTTP 201 Created com JSON                     │
└─────────────────────────────────────────────────────────────┘
```

### Representação Visual

```
   HTTP Request (JSON)
         │
         ▼
   ┌──────────┐
   │Controller│ ◄─── Adaptador de Entrada
   └────┬─────┘
        │ DTO → Domain
        ▼
   ┌──────────┐
   │ UseCase  │ ◄─── Porta de Entrada
   └────┬─────┘
        │
        ▼
   ┌──────────┐
   │ Service  │ ◄─── Núcleo (Implementação)
   └────┬─────┘
        │ Chama
        ▼
   ┌──────────┐
   │Repository│ ◄─── Porta de Saída
   └────┬─────┘
        │
        ▼
   ┌──────────┐
   │   JPA    │ ◄─── Adaptador de Saída
   └────┬─────┘
        │
        ▼
   Database (H2)
```

---

## 🔧 Componentes da Aplicação

### 1. Domínio

#### ClienteDomain.java

Entidade de domínio que representa um cliente:

```java
public class ClienteDomain {
    private Long id;
    private String nome;
    private String email;
    private Integer idade;
    private String cpf;
    private BigDecimal rendaMensal;
    private String dataVencimentoFatura;
    private EnderecoDomain endereco;
    private CartaoDomain cartao;  // Relacionamento com cartão
}
```

**Características:**
- Classe POJO pura (Plain Old Java Object)
- Sem anotações de frameworks (Spring, JPA)
- Contém apenas lógica de negócio

#### CartaoDomain.java

Entidade de domínio que representa um cartão:

```java
public class CartaoDomain {
    private Long id;
    private String numero;       // Ex: 5425-2334-3010-9903
    private String bandeira;     // Ex: Mastercard, Visa
    private String cvv;          // Ex: 326
    private BigDecimal limite;   // Ex: 5000.00
}
```

#### EnderecoDomain.java

Entidade de domínio que representa um endereço:

```java
public class EnderecoDomain {
    private Long id;
    private String rua;
    private Integer numero;
    private String complemento;
    private String cidade;
    private String estado;       // Ex: SP, RJ
    private String cep;
}
```

### 2. Exceções de Domínio

#### ClienteJaPossuiCartaoException

Lançada quando um cliente tenta solicitar um cartão mas já possui um:

```java
public class ClienteJaPossuiCartaoException extends RuntimeException {
    public ClienteJaPossuiCartaoException(String email) {
        super("Cliente com email " + email + " já possui um cartão.");
    }
}
```

#### ClienteNaoEncontradoException

Lançada quando um cliente não é encontrado pelo CPF:

```java
public class ClienteNaoEncontradoException extends RuntimeException {
    public ClienteNaoEncontradoException(String cpf) {
        super("Cliente com CPF " + cpf + " não encontrado.");
    }
}
```

### 3. Portas de Entrada (Use Cases)

#### SolicitarCartaoUseCase

Define o contrato para solicitar um cartão:

```java
public interface SolicitarCartaoUseCase {
    ClienteDomain solicitarCartao(ClienteDomain cliente);
}
```

#### BuscarClientePorCpfUseCase

Define o contrato para buscar cliente por CPF:

```java
public interface BuscarClientePorCpfUseCase {
    ClienteDomain buscarPorCpf(String cpf);
}
```

### 4. Porta de Saída

#### ClienteRepository

Define o contrato de persistência:

```java
public interface ClienteRepository {
    ClienteDomain salvar(ClienteDomain cliente);
    Optional<ClienteDomain> buscarPorCpf(String cpf);
    boolean existePorEmail(String email);
}
```

### 5. Serviços (Implementação dos Use Cases)

#### IClienteServiceImpl

Implementa os use cases e contém a lógica de negócio:

```java
@Service
public class IClienteServiceImpl implements 
    SolicitarCartaoUseCase, BuscarClientePorCpfUseCase {
    
    private final ClienteRepository clienteRepository;
    private final GeraDadosCartaoService geraDadosCartaoService;
    
    @Override
    public ClienteDomain solicitarCartao(ClienteDomain cliente) {
        // Regra 1: Verifica se email já existe
        if (clienteRepository.existePorEmail(cliente.getEmail())) {
            throw new ClienteJaPossuiCartaoException(cliente.getEmail());
        }
        
        // Regra 2: Gera dados do cartão
        CartaoDomain cartao = geraDadosCartaoService.gerar(cliente);
        cliente.setCartao(cartao);
        
        // Persiste cliente com cartão
        return clienteRepository.salvar(cliente);
    }
    
    @Override
    public ClienteDomain buscarPorCpf(String cpf) {
        return clienteRepository.buscarPorCpf(cpf)
            .orElseThrow(() -> new ClienteNaoEncontradoException(cpf));
    }
}
```

#### GeraDadosCartaoService

Serviço de domínio que gera dados do cartão:

```java
@Service
public class GeraDadosCartaoService {
    
    public CartaoDomain gerar(ClienteDomain cliente) {
        CartaoDomain cartao = new CartaoDomain();
        cartao.setNumero(gerarNumeroCartao());
        cartao.setBandeira(gerarBandeira());
        cartao.setCvv(gerarCVV());
        cartao.setLimite(calcularLimite(cliente.getRendaMensal()));
        return cartao;
    }
    
    private String gerarNumeroCartao() {
        // Gera número de cartão fictício
        Random random = new Random();
        return String.format("%04d-%04d-%04d-%04d", 
            random.nextInt(10000), random.nextInt(10000),
            random.nextInt(10000), random.nextInt(10000));
    }
    
    private String gerarBandeira() {
        String[] bandeiras = {"Mastercard", "Visa", "Elo"};
        return bandeiras[new Random().nextInt(bandeiras.length)];
    }
    
    private String gerarCVV() {
        return String.format("%03d", new Random().nextInt(1000));
    }
    
    private BigDecimal calcularLimite(BigDecimal rendaMensal) {
        // Limite = 30% da renda mensal
        return rendaMensal.multiply(new BigDecimal("0.30"))
            .setScale(2, RoundingMode.HALF_UP);
    }
}
```

### 6. Adaptadores de Entrada

#### ClienteController

Controller REST que expõe os endpoints:

```java
@RestController
@RequestMapping("/cliente")
public class ClienteController {
    
    private final SolicitarCartaoUseCase solicitarCartaoUseCase;
    private final BuscarClientePorCpfUseCase buscarClientePorCpfUseCase;
    private final ClienteMapper mapper;
    
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> solicitarCartao(
            @Valid @RequestBody ClienteRequestDTO request) {
        
        // Converte DTO → Domain
        ClienteDomain clienteDomain = mapper.toDomain(request);
        
        // Executa use case
        ClienteDomain resultado = solicitarCartaoUseCase
            .solicitarCartao(clienteDomain);
        
        // Converte Domain → DTO
        ClienteResponseDTO response = mapper.toResponseDTO(resultado);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    public ResponseEntity<ClienteResponseDTO> buscarPorCpf(
            @RequestParam String cpf) {
        
        ClienteDomain cliente = buscarClientePorCpfUseCase.buscarPorCpf(cpf);
        ClienteResponseDTO response = mapper.toResponseDTO(cliente);
        
        return ResponseEntity.ok(response);
    }
}
```

#### GlobalExceptionHandler

Trata exceções e retorna respostas HTTP adequadas:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ClienteJaPossuiCartaoException.class)
    public ResponseEntity<ErrorResponse> handleClienteJaPossuiCartao(
            ClienteJaPossuiCartaoException ex) {
        ErrorResponse error = new ErrorResponse(
            409,
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
    
    @ExceptionHandler(ClienteNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleClienteNaoEncontrado(
            ClienteNaoEncontradoException ex) {
        ErrorResponse error = new ErrorResponse(
            404,
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        ValidationErrorResponse response = new ValidationErrorResponse(
            400,
            "Erro de validação",
            LocalDateTime.now(),
            errors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
```

### 7. DTOs (Data Transfer Objects)

#### ClienteRequestDTO

DTO de entrada com validações:

```java
public class ClienteRequestDTO {
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;
    
    @NotNull(message = "Idade é obrigatória")
    @Min(value = 18, message = "Idade mínima é 18 anos")
    @Max(value = 120, message = "Idade máxima é 120 anos")
    private Integer idade;
    
    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11}",
             message = "CPF deve estar no formato XXX.XXX.XXX-XX ou 11 dígitos")
    private String cpf;
    
    @NotNull(message = "Renda mensal é obrigatória")
    @Positive(message = "Renda mensal deve ser positiva")
    private BigDecimal rendaMensal;
    
    @NotBlank(message = "Data de vencimento da fatura é obrigatória")
    private String dataVencimentoFatura;
    
    @NotNull(message = "Endereço é obrigatório")
    @Valid
    private EnderecoRequestDTO endereco;
}
```

#### ClienteResponseDTO

DTO de saída:

```java
public class ClienteResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private Integer idade;
    private String cpf;
    private BigDecimal rendaMensal;
    private String dataVencimentoFatura;
    private EnderecoRequestDTO endereco;
    private CartaoResponseDTO cartao;  // Inclui dados do cartão gerado
}
```

### 8. Adaptadores de Saída

#### ClienteRepositoryImpl

Implementação JPA do repositório:

```java
@Component
public class ClienteRepositoryImpl implements ClienteRepository {
    
    private final IClienteJpaRepository jpaRepository;
    private final ClienteMapper mapper;
    
    @Override
    public ClienteDomain salvar(ClienteDomain cliente) {
        ClienteEntity entity = mapper.toEntity(cliente);
        ClienteEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
    
    @Override
    public Optional<ClienteDomain> buscarPorCpf(String cpf) {
        return jpaRepository.findByCpf(cpf)
            .map(mapper::toDomain);
    }
    
    @Override
    public boolean existePorEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
}
```

#### ClienteEntity

Entidade JPA:

```java
@Entity
@Table(name = "clientes")
public class ClienteEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    
    @Column(unique = true)
    private String email;
    
    private Integer idade;
    
    @Column(unique = true)
    private String cpf;
    
    private BigDecimal rendaMensal;
    private String dataVencimentoFatura;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id")
    private EnderecoEntity endereco;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cartao_id")
    private CartaoEntity cartao;
}
```

---

## 💡 Exemplos Práticos

### Exemplo 1: Solicitar Cartão (Sucesso)

**Request:**

```http
POST http://localhost:8080/cliente
Content-Type: application/json

{
  "nome": "Maria Silva",
  "email": "maria@email.com",
  "idade": 28,
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
}
```

**Response (201 Created):**

```json
{
  "id": 1,
  "nome": "Maria Silva",
  "email": "maria@email.com",
  "idade": 28,
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
  },
  "cartao": {
    "numero": "5425-2334-3010-9903",
    "bandeira": "Mastercard",
    "cvv": "326",
    "limite": 1500.00
  }
}
```

### Exemplo 2: Cliente Já Possui Cartão (Erro)

**Request:**

```http
POST http://localhost:8080/cliente
Content-Type: application/json

{
  "nome": "Maria Silva",
  "email": "maria@email.com",
  ...
}
```

**Response (409 Conflict):**

```json
{
  "status": 409,
  "message": "Cliente com email maria@email.com já possui um cartão.",
  "timestamp": "2026-05-14T01:00:00"
}
```

### Exemplo 3: Erro de Validação

**Request:**

```http
POST http://localhost:8080/cliente
Content-Type: application/json

{
  "nome": "Jo",
  "email": "email-invalido",
  "idade": 15,
  "cpf": "123",
  "rendaMensal": -1000
}
```

**Response (400 Bad Request):**

```json
{
  "status": 400,
  "message": "Erro de validação",
  "timestamp": "2026-05-14T01:00:00",
  "errors": {
    "nome": "Nome deve ter entre 3 e 100 caracteres",
    "email": "Email deve ser válido",
    "idade": "Idade mínima é 18 anos",
    "cpf": "CPF deve estar no formato XXX.XXX.XXX-XX ou 11 dígitos",
    "rendaMensal": "Renda mensal deve ser positiva",
    "endereco": "Endereço é obrigatório"
  }
}
```

### Exemplo 4: Buscar Cliente por CPF

**Request:**

```http
GET http://localhost:8080/cliente?cpf=123.456.789-10
```

**Response (200 OK):**

```json
{
  "id": 1,
  "nome": "Maria Silva",
  "email": "maria@email.com",
  "cpf": "123.456.789-10",
  "cartao": {
    "numero": "5425-2334-3010-9903",
    "bandeira": "Mastercard",
    "limite": 1500.00
  }
}
```

---

## 📋 Regras de Negócio

### 1. Solicitação de Cartão

- ✅ Cliente deve ter **no mínimo 18 anos**
- ✅ Email deve ser **único** no sistema
- ✅ CPF deve ser **único** no sistema
- ✅ Cliente só pode ter **um cartão**
- ✅ Renda mensal deve ser **positiva**

### 2. Geração de Dados do Cartão

- ✅ **Número do cartão**: gerado aleatoriamente no formato XXXX-XXXX-XXXX-XXXX
- ✅ **Bandeira**: escolhida aleatoriamente entre Mastercard, Visa e Elo
- ✅ **CVV**: gerado aleatoriamente (3 dígitos)
- ✅ **Limite**: calculado como **30% da renda mensal** do cliente

**Exemplo de cálculo de limite:**

```
Renda mensal: R$ 5.000,00
Limite = 5.000 × 0,30 = R$ 1.500,00
```

### 3. Validações de Entrada

#### Cliente

- Nome: 3-100 caracteres
- Email: formato válido
- Idade: 18-120 anos
- CPF: formato XXX.XXX.XXX-XX ou 11 dígitos
- Renda mensal: valor positivo
- Data vencimento fatura: obrigatória

#### Endereço

- Rua: 3-200 caracteres
- Número: positivo
- Cidade: 2-100 caracteres
- Estado: 2 caracteres (sigla UF)
- CEP: formato XXXXX-XXX ou 8 dígitos

---

## 🧪 Como Testar

### 1. Compilar o Projeto

```powershell
.\gradlew clean build
```

### 2. Executar a Aplicação

```powershell
.\gradlew bootRun
```

A aplicação estará disponível em: `http://localhost:8080`

### 3. Testar com cURL

#### Solicitar Cartão (Sucesso)

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

#### Buscar Cliente por CPF

```bash
curl -X GET "http://localhost:8080/cliente?cpf=123.456.789-10"
```

#### Testar Email Duplicado (Erro 409)

```bash
# Execute novamente o POST com o mesmo email
curl -X POST http://localhost:8080/cliente \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Outro Cliente",
    "email": "joao@email.com",
    ...
  }'
```

#### Testar Validação (Erro 400)

```bash
curl -X POST http://localhost:8080/cliente \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Jo",
    "email": "email-invalido",
    "idade": 15,
    "cpf": "123",
    "rendaMensal": -1000
  }'
```

### 4. Testar com Postman/Insomnia

Importe a collection ou crie as requests manualmente:

1. **POST** `http://localhost:8080/cliente` - Criar cliente
2. **GET** `http://localhost:8080/cliente?cpf=XXX.XXX.XXX-XX` - Buscar cliente

### 5. Acessar Console H2

O banco H2 tem um console web disponível em:

```
http://localhost:8080/h2-console
```

**Credenciais:**
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (vazio)

**Queries úteis:**

```sql
-- Ver todos os clientes
SELECT * FROM clientes;

-- Ver todos os cartões
SELECT * FROM cartoes;

-- Ver clientes com cartões
SELECT c.nome, c.email, ca.numero, ca.bandeira, ca.limite
FROM clientes c
INNER JOIN cartoes ca ON c.cartao_id = ca.id;
```

---

## 🎯 Benefícios Alcançados no Projeto

### 1. Testabilidade

O domínio pode ser testado **sem subir Spring** ou banco de dados:

```java
@Test
void deveCalcularLimiteCorretamente() {
    // Arrange
    BigDecimal renda = new BigDecimal("5000");
    GeraDadosCartaoService service = new GeraDadosCartaoService();
    ClienteDomain cliente = new ClienteDomain();
    cliente.setRendaMensal(renda);
    
    // Act
    CartaoDomain cartao = service.gerar(cliente);
    
    // Assert
    assertEquals(new BigDecimal("1500.00"), cartao.getLimite());
}
```

### 2. Independência de Framework

- O domínio **não conhece Spring**, JPA ou qualquer framework
- As entidades de domínio são POJOs puros
- As regras de negócio estão isoladas

### 3. Flexibilidade para Mudanças

Trocar JPA por MongoDB é simples:

1. Criar `ClienteRepositoryMongoImpl`
2. Atualizar configuração do Spring
3. Domínio permanece intocado!

### 4. Clareza e Organização

- Cada camada tem responsabilidade bem definida
- Use cases expressam intenções claras
- Fácil encontrar onde está cada funcionalidade

### 5. Tratamento de Erros Robusto

- Exceções específicas de domínio
- Handler global centralizado
- Mensagens de erro claras para o cliente

---

## 📖 Próximos Passos Sugeridos

### 1. Testes Automatizados

- [ ] Testes unitários do domínio
- [ ] Testes unitários dos services
- [ ] Testes de integração com banco
- [ ] Testes de contrato da API

### 2. Novos Use Cases

- [ ] Atualizar limite do cartão
- [ ] Consultar fatura
- [ ] Bloquear/desbloquear cartão
- [ ] Atualizar dados cadastrais

### 3. Melhorias Técnicas

- [ ] Adicionar Swagger/OpenAPI para documentação da API
- [ ] Implementar paginação na busca de clientes
- [ ] Adicionar logs estruturados (SLF4J + Logback)
- [ ] Adicionar métricas (Actuator + Micrometer)

### 4. Segurança

- [ ] Implementar autenticação (JWT)
- [ ] Adicionar autorização (Spring Security)
- [ ] Criptografar dados sensíveis (número do cartão, CVV)
- [ ] Adicionar rate limiting

### 5. Observabilidade

- [ ] Adicionar distributed tracing (OpenTelemetry)
- [ ] Implementar health checks
- [ ] Adicionar dashboards de métricas

---

## 📚 Referências

### Arquitetura Hexagonal

- **Artigo original de Alistair Cockburn**: https://alistair.cockburn.us/hexagonal-architecture/
- **Clean Architecture (Robert C. Martin)**: Livro sobre arquiteturas limpas
- **Domain-Driven Design (Eric Evans)**: Fundamentos de design orientado ao domínio

### Spring Boot

- **Documentação oficial**: https://spring.io/projects/spring-boot
- **Spring Data JPA**: https://spring.io/projects/spring-data-jpa
- **Bean Validation**: https://beanvalidation.org/

### Ferramentas

- **MapStruct**: https://mapstruct.org/
- **Lombok**: https://projectlombok.org/
- **Gradle**: https://gradle.org/

---

## 👥 Conclusão

A **Arquitetura Hexagonal** permite criar aplicações:

- ✅ **Testáveis**: sem dependências de frameworks
- ✅ **Flexíveis**: fáceis de mudar e evoluir
- ✅ **Organizadas**: código claro e bem estruturado
- ✅ **Independentes**: sem acoplamento com tecnologias específicas

Este projeto demonstra como aplicar esses conceitos em uma aplicação **Spring Boot** real, mantendo o domínio puro e as dependências bem isoladas.

