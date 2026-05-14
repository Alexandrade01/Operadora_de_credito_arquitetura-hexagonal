package com.example.operadoracartaocredito.application.ports.out;

import com.example.operadoracartaocredito.application.domain.ClienteDomain;

import java.util.Optional;

/**
 * Porta de saída para persistência de clientes
 */
public interface ClienteRepository {

    /**
     * Salva um cliente no banco de dados
     * @param cliente Cliente a ser salvo
     * @return Cliente salvo com ID gerado
     */
    ClienteDomain salvar(ClienteDomain cliente);

    /**
     * Verifica se existe um cliente com o email informado
     * @param email Email a ser verificado
     * @return true se existe cliente com esse email
     */
    boolean existePorEmail(String email);

    /**
     * Busca um cliente pelo CPF
     * @param cpf CPF do cliente
     * @return Optional com o cliente, se encontrado
     */
    Optional<ClienteDomain> buscarPorCpf(String cpf);
}

