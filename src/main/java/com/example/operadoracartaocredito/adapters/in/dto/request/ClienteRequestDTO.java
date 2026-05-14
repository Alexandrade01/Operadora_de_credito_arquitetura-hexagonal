package com.example.operadoracartaocredito.adapters.in.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * DTO para requisição de criação de cliente
 */
public record ClienteRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
        String nome,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email deve ser válido")
        String email,

        @NotNull(message = "Idade é obrigatória")
        @Min(value = 18, message = "Idade mínima é 18 anos")
        @Max(value = 120, message = "Idade máxima é 120 anos")
        Integer idade,

        @NotBlank(message = "CPF é obrigatório")
        @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11}",
                message = "CPF deve estar no formato XXX.XXX.XXX-XX ou conter 11 dígitos")
        String cpf,

        @NotNull(message = "Endereço é obrigatório")
        @Valid
        EnderecoRequestDTO endereco,

        @Positive(message = "Renda mensal deve ser positiva")
        @NotNull(message = "Renda mensal é obrigatória")
        double rendaMensal,

        @NotBlank(message = "Data de vencimento da fatura é obrigatória")
        String dataVencimentoFatura
) {}

