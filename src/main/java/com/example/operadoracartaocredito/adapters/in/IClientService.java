package com.example.operadoracartaocredito.adapters.in;

import com.example.operadoracartaocredito.application.domain.ClienteDomain;

public interface IClientService {

    ClienteDomain solicitarCartao(ClienteDomain cliente);

    ClienteDomain buscarPorCpf(String cpf);
}
