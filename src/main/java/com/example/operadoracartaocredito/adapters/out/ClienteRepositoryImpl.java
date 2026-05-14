package com.example.operadoracartaocredito.adapters.out;

import com.example.operadoracartaocredito.adapters.mapper.ClienteMapper;
import com.example.operadoracartaocredito.adapters.out.entities.ClienteEntity;
import com.example.operadoracartaocredito.adapters.out.repositories.IClienteJpaRepository;
import com.example.operadoracartaocredito.application.domain.ClienteDomain;
import com.example.operadoracartaocredito.application.ports.out.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adaptador de saída que implementa a persistência de clientes usando JPA
 */
@Component
@RequiredArgsConstructor
public class ClienteRepositoryImpl implements ClienteRepository {

    private final IClienteJpaRepository repository;
    private final ClienteMapper clienteMapper;

    @Override
    public ClienteDomain salvar(ClienteDomain clienteDomain) {
        // Converte Domain -> Entity
        ClienteEntity clienteEntity = clienteMapper.paraEntity(clienteDomain);

        // Salva usando JPA
        ClienteEntity clienteSalvo = repository.save(clienteEntity);

        // Converte Entity -> Domain e retorna
        return clienteMapper.paraDomain(clienteSalvo);
    }

    @Override
    public boolean existePorEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public Optional<ClienteDomain> buscarPorCpf(String cpf) {
        return repository.findByCpf(cpf)
                .map(clienteMapper::paraDomain);
    }
}
