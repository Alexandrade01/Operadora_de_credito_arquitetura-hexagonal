package com.example.operadoracartaocredito.application.domain;

public class EnderecoDomain {

    private Long id;
    private String rua;
    private Long numero;
    private String complemento;
    private String cidade;
    private String estado;
    private String cep;
    private ClienteDomain cliente;

    public EnderecoDomain() {
    }

    public EnderecoDomain(Long id, ClienteDomain cliente, String cep, String estado,
                          String cidade, String complemento, Long numero, String rua) {
        this.id = id;
        this.cliente = cliente;
        this.cep = cep;
        this.estado = estado;
        this.cidade = cidade;
        this.complemento = complemento;
        this.numero = numero;
        this.rua = rua;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public ClienteDomain getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDomain cliente) {
        this.cliente = cliente;
    }
}


