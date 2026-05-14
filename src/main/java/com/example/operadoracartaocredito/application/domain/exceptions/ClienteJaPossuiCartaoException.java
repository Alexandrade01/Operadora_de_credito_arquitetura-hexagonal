package com.example.operadoracartaocredito.application.domain.exceptions;

/**
 * Exceção lançada quando um cliente tenta solicitar um cartão mas já possui um
 */
public class ClienteJaPossuiCartaoException extends RuntimeException {
    
    public ClienteJaPossuiCartaoException(String email) {
        super("Cliente com email " + email + " já possui um cartão.");
    }
}

