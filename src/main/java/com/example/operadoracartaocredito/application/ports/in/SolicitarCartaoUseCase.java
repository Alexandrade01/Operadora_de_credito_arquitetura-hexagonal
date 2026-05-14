package com.example.operadoracartaocredito.application.ports.in;

import com.example.operadoracartaocredito.application.domain.ClienteDomain;

/**
 * Porta de entrada (use case) para solicitar um novo cartão de crédito
 */
public interface SolicitarCartaoUseCase {
    
    /**
     * Solicita um cartão de crédito para um cliente
     * @param cliente Dados do cliente solicitante
     * @return Cliente com cartão gerado
     * @throws com.example.operadoracartaocredito.application.domain.exceptions.ClienteJaPossuiCartaoException se o cliente já possui cartão
     */
    ClienteDomain executar(ClienteDomain cliente);
}

