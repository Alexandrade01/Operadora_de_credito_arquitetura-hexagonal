package com.example.operadoracartaocredito.application.services;

import com.example.operadoracartaocredito.application.domain.CartaoDomain;
import com.example.operadoracartaocredito.application.domain.ClienteDomain;
import com.example.operadoracartaocredito.application.domain.exceptions.ClienteJaPossuiCartaoException;
import com.example.operadoracartaocredito.application.domain.exceptions.ClienteNaoEncontradoException;
import com.example.operadoracartaocredito.application.ports.in.BuscarClientePorCpfUseCase;
import com.example.operadoracartaocredito.application.ports.in.SolicitarCartaoUseCase;
import com.example.operadoracartaocredito.application.ports.out.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementação dos casos de uso relacionados a Cliente
 */
@Service
@RequiredArgsConstructor
public class IClienteServiceImpl implements SolicitarCartaoUseCase, BuscarClientePorCpfUseCase {

    private final ClienteRepository clienteRepository;
    private final GeraDadosCartaoService geraCartao;

    @Override
    public ClienteDomain executar(ClienteDomain cliente) {
        if (clienteRepository.existePorEmail(cliente.getEmail())) {
            throw new ClienteJaPossuiCartaoException(cliente.getEmail());
        }

        CartaoDomain cartao = geraCartao.gerarParaCliente(cliente);
        cliente.setCartao(cartao);
        return clienteRepository.salvar(cliente);
    }

    @Override
    public ClienteDomain executar(String cpf) {
        return clienteRepository.buscarPorCpf(cpf)
                .orElseThrow(() -> new ClienteNaoEncontradoException(cpf));
    }
}

