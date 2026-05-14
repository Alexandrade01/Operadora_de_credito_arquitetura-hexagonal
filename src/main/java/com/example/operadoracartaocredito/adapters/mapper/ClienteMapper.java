package com.example.operadoracartaocredito.adapters.mapper;

import com.example.operadoracartaocredito.adapters.in.dto.request.ClienteRequestDTO;
import com.example.operadoracartaocredito.adapters.in.dto.response.ClienteResponseDTO;
import com.example.operadoracartaocredito.adapters.out.entities.CartaoEntity;
import com.example.operadoracartaocredito.adapters.out.entities.ClienteEntity;
import com.example.operadoracartaocredito.application.domain.CartaoDomain;
import com.example.operadoracartaocredito.application.domain.ClienteDomain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    ClienteEntity paraEntity(ClienteDomain clienteDomain);

    ClienteDomain paraDomain(ClienteEntity clienteEntity);

    @Mapping(target = "cartao", expression = "java(toCartaoDomain(cliente))")
    ClienteDomain toDomain(ClienteRequestDTO cliente);

    ClienteResponseDTO toResponse(ClienteDomain cliente);

    @Mapping(source = "dataVencimentoFatura", target = "dataVencimentoFatura")
    CartaoDomain toCartaoDomain(ClienteRequestDTO clienteRequestDTO);
}
