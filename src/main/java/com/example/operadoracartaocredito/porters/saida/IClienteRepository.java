package com.example.operadoracartaocredito.porters.saida;

import com.example.operadoracartaocredito.application.domain.ClienteDomain;

import java.util.Optional;

public interface IClienteRepository {

    ClienteDomain salvar(ClienteDomain cliente);

    Boolean buscaUsuarioPorEmail(String email);

    Optional<ClienteDomain> buscarUsuarioPorCpf(String cpf);

}
