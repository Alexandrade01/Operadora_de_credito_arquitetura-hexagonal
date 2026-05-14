package com.example.operadoracartaocredito.application.domain.exceptions;

/**
 * Exceção lançada quando um cliente não é encontrado
 */
public class ClienteNaoEncontradoException extends RuntimeException {

    public ClienteNaoEncontradoException(String cpf) {
        super("Cliente com CPF " + cpf + " não encontrado.");
    }
}

