package com.example.operadoracartaocredito.application.ports.in;

import com.example.operadoracartaocredito.application.domain.ClienteDomain;

/**
 * Porta de entrada (use case) para buscar cliente por CPF
 */
public interface BuscarClientePorCpfUseCase {
    
    /**
     * Busca um cliente pelo CPF
     * @param cpf CPF do cliente
     * @return Dados do cliente
     * @throws com.example.operadoracartaocredito.application.domain.exceptions.ClienteNaoEncontradoException se o cliente não for encontrado
     */
    ClienteDomain executar(String cpf);
}

