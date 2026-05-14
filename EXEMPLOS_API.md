# Exemplos de Requisições HTTP

## POST /cliente - Criar Cliente com Cartão

### Sucesso (201 Created)
```json
POST http://localhost:8080/cliente
Content-Type: application/json

{
  "nome": "João da Silva",
  "email": "joao.silva@email.com",
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
}
```

### Erro - Email Duplicado (409 Conflict)
```json
POST http://localhost:8080/cliente
Content-Type: application/json

{
  "nome": "Maria Souza",
  "email": "joao.silva@email.com",
  "idade": 25,
  "cpf": "987.654.321-00",
  "rendaMensal": 3000.00,
  "dataVencimentoFatura": "15",
  "endereco": {
    "rua": "Avenida Paulista",
    "numero": 1000,
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "01310-100"
  }
}
```

Resposta esperada:
```json
{
  "status": 409,
  "message": "Cliente com email joao.silva@email.com já possui um cartão.",
  "timestamp": "2026-05-13T10:30:00"
}
```

### Erro - Validação (400 Bad Request)
```json
POST http://localhost:8080/cliente
Content-Type: application/json

{
  "nome": "Jo",
  "email": "email-invalido",
  "idade": 15,
  "cpf": "123",
  "rendaMensal": -1000,
  "dataVencimentoFatura": "",
  "endereco": {
    "rua": "",
    "numero": -1,
    "cidade": "",
    "estado": "S",
    "cep": "123"
  }
}
```

Resposta esperada:
```json
{
  "status": 400,
  "message": "Erro de validação",
  "timestamp": "2026-05-13T10:35:00",
  "errors": {
    "nome": "Nome deve ter entre 3 e 100 caracteres",
    "email": "Email deve ser válido",
    "idade": "Idade mínima é 18 anos",
    "cpf": "CPF deve estar no formato XXX.XXX.XXX-XX ou conter 11 dígitos",
    "rendaMensal": "Renda mensal deve ser positiva",
    "endereco.rua": "Rua é obrigatória",
    "endereco.numero": "Número deve ser positivo",
    "endereco.cidade": "Cidade é obrigatória",
    "endereco.estado": "Estado deve ter 2 caracteres (sigla)",
    "endereco.cep": "CEP deve estar no formato XXXXX-XXX ou conter 8 dígitos"
  }
}
```

---

## GET /cliente - Buscar Cliente por CPF

### Sucesso (200 OK)
```
GET http://localhost:8080/cliente?cpf=123.456.789-10
```

Resposta esperada:
```json
{
  "id": 1,
  "nome": "João da Silva",
  "email": "joao.silva@email.com",
  "idade": 30,
  "cpf": "123.456.789-10",
  "rendaMensal": 5000.00,
  "endereco": {
    "id": 1,
    "rua": "Rua das Flores",
    "numero": 123,
    "complemento": "Apto 101",
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "01234-567"
  },
  "cartao": {
    "id": 1,
    "numero": "4000123456789012",
    "dataExpiracao": "2029-05-01",
    "limite": 5000.00,
    "availableLimit": 5000.00,
    "dataVencimentoFatura": 10
  }
}
```

### Erro - Cliente Não Encontrado (404 Not Found)
```
GET http://localhost:8080/cliente?cpf=999.999.999-99
```

Resposta esperada:
```json
{
  "status": 404,
  "message": "Cliente com CPF 999.999.999-99 não encontrado.",
  "timestamp": "2026-05-13T10:40:00"
}
```

---

## Testando com cURL (PowerShell)

### POST - Criar Cliente
```powershell
curl.exe -X POST http://localhost:8080/cliente `
  -H "Content-Type: application/json" `
  -d '{
    \"nome\": \"João da Silva\",
    \"email\": \"joao.silva@email.com\",
    \"idade\": 30,
    \"cpf\": \"123.456.789-10\",
    \"rendaMensal\": 5000.00,
    \"dataVencimentoFatura\": \"10\",
    \"endereco\": {
      \"rua\": \"Rua das Flores\",
      \"numero\": 123,
      \"complemento\": \"Apto 101\",
      \"cidade\": \"São Paulo\",
      \"estado\": \"SP\",
      \"cep\": \"01234-567\"
    }
  }'
```

### GET - Buscar Cliente
```powershell
curl.exe -X GET "http://localhost:8080/cliente?cpf=123.456.789-10"
```

---

## Testando com Invoke-WebRequest (PowerShell)

### POST - Criar Cliente
```powershell
$body = @{
    nome = "João da Silva"
    email = "joao.silva@email.com"
    idade = 30
    cpf = "123.456.789-10"
    rendaMensal = 5000.00
    dataVencimentoFatura = "10"
    endereco = @{
        rua = "Rua das Flores"
        numero = 123
        complemento = "Apto 101"
        cidade = "São Paulo"
        estado = "SP"
        cep = "01234-567"
    }
} | ConvertTo-Json

Invoke-WebRequest -Uri "http://localhost:8080/cliente" `
  -Method POST `
  -ContentType "application/json" `
  -Body $body
```

### GET - Buscar Cliente
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/cliente?cpf=123.456.789-10" -Method GET
```

