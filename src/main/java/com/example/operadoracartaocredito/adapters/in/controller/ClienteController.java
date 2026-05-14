package com.example.operadoracartaocredito.adapters.in.controller;

import com.example.operadoracartaocredito.adapters.in.dto.request.ClienteRequestDTO;
import com.example.operadoracartaocredito.adapters.in.dto.response.ClienteResponseDTO;
import com.example.operadoracartaocredito.adapters.mapper.ClienteMapper;
import com.example.operadoracartaocredito.application.domain.ClienteDomain;
import com.example.operadoracartaocredito.application.ports.in.BuscarClientePorCpfUseCase;
import com.example.operadoracartaocredito.application.ports.in.SolicitarCartaoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para operações relacionadas a clientes e cartões
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/cliente")
public class ClienteController {

    private final SolicitarCartaoUseCase solicitarCartaoUseCase;
    private final BuscarClientePorCpfUseCase buscarClientePorCpfUseCase;
    private final ClienteMapper mapper;

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> solicitaCartao(
            @RequestBody @Validated ClienteRequestDTO clienteRequestDTO) {

        // Converte DTO -> Domain
        ClienteDomain cliente = mapper.toDomain(clienteRequestDTO);

        // Executa caso de uso
        ClienteDomain clienteSalvo = solicitarCartaoUseCase.executar(cliente);

        // Converte Domain -> DTO de resposta
        ClienteResponseDTO response = mapper.toResponse(clienteSalvo);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ClienteResponseDTO> buscaClientePorCpf(@RequestParam String cpf) {
        ClienteDomain cliente = buscarClientePorCpfUseCase.executar(cpf);
        return ResponseEntity.ok(mapper.toResponse(cliente));
    }
}
