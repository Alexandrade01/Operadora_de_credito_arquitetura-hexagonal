package com.example.operadoracartaocredito.adapters.in.dto.request;

import jakarta.validation.constraints.*;

/**
 * DTO para requisição de endereço
 */
public record EnderecoRequestDTO(

        Long id,

        @NotBlank(message = "Rua é obrigatória")
        @Size(min = 3, max = 200, message = "Rua deve ter entre 3 e 200 caracteres")
        String rua,

        @NotNull(message = "Número é obrigatório")
        @Positive(message = "Número deve ser positivo")
        Long numero,

        String complemento,

        @NotBlank(message = "Cidade é obrigatória")
        @Size(min = 2, max = 100, message = "Cidade deve ter entre 2 e 100 caracteres")
        String cidade,

        @NotBlank(message = "Estado é obrigatório")
        @Size(min = 2, max = 2, message = "Estado deve ter 2 caracteres (sigla)")
        String estado,

        @NotBlank(message = "CEP é obrigatório")
        @Pattern(regexp = "\\d{5}-\\d{3}|\\d{8}",
                message = "CEP deve estar no formato XXXXX-XXX ou conter 8 dígitos")
        String cep
) {}
