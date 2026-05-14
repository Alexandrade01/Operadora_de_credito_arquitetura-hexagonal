package com.example.operadoracartaocredito.application.domain;

public class ClienteDomain {

    private Long id;
    private String nome;
    private String email;
    private Integer idade;
    private String cpf;
    private double rendaMensal;
    private EnderecoDomain endereco;
    private CartaoDomain cartao;

    public ClienteDomain(Long id, String email, String nome, Integer idade, String cpf,
                         double rendaMensal, EnderecoDomain endereco, CartaoDomain cartao) {
        this.id = id;
        this.email = email;
        this.nome = nome;
        this.idade = idade;
        this.cpf = cpf;
        this.rendaMensal = rendaMensal;
        this.endereco = endereco;
        this.cartao = cartao;
    }

    public ClienteDomain() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public double getRendaMensal() {
        return rendaMensal;
    }

    public void setRendaMensal(double rendaMensal) {
        this.rendaMensal = rendaMensal;
    }

    public EnderecoDomain getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoDomain endereco) {
        this.endereco = endereco;
    }

    public CartaoDomain getCartao() {
        return cartao;
    }

    public void setCartao(CartaoDomain cartao) {
        this.cartao = cartao;
    }
}
