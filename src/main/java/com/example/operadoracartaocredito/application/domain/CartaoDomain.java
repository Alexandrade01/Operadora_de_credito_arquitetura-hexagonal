package com.example.operadoracartaocredito.application.domain;


import java.time.LocalDate;

public class CartaoDomain {

    private Long id;
    private String numero;
    private LocalDate dataExpiracao;
    private String cvv;
    private double limite;
    private double availableLimit;
    private LocalDate ultimaAlteracaoLimite;
    private Integer dataVencimentoFatura;
    private ClienteDomain cliente;

    // Construtor vazio necessário para MapStruct
    public CartaoDomain() {
    }

    public CartaoDomain(Long id, String numero, LocalDate dataExpiracao, ClienteDomain cliente,
                        Integer dataVencimentoFatura, LocalDate ultimaAlteracaoLimite,
                        double availableLimit, double limite, String cvv) {
        this.id = id;
        this.numero = numero;
        this.dataExpiracao = dataExpiracao;
        this.cliente = cliente;
        this.dataVencimentoFatura = dataVencimentoFatura;
        this.ultimaAlteracaoLimite = ultimaAlteracaoLimite;
        this.availableLimit = availableLimit;
        this.limite = limite;
        this.cvv = cvv;
    }

    public CartaoDomain(String numeroCartao, LocalDate dataExpiracao, String cvv, double limiteCredito, ClienteDomain cliente, LocalDate ultimaAlteracaoLimite, Integer dataVencimentoFatura) {
        this.numero = numeroCartao;
        this.dataExpiracao = dataExpiracao;
        this.cvv = cvv;
        this.limite = limiteCredito;
        this.availableLimit = limiteCredito;  // Limite disponível = limite total inicialmente
        this.cliente = cliente;
        this.ultimaAlteracaoLimite = ultimaAlteracaoLimite;
        this.dataVencimentoFatura = dataVencimentoFatura;
    }


    public Long getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public LocalDate getDataExpiracao() {
        return dataExpiracao;
    }

    public String getCvv() {
        return cvv;
    }

    public double getLimite() {
        return limite;
    }

    public double getAvailableLimit() {
        return availableLimit;
    }

    public LocalDate getUltimaAlteracaoLimite() {
        return ultimaAlteracaoLimite;
    }

    public Integer getDataVencimentoFatura() {
        return dataVencimentoFatura;
    }

    public ClienteDomain getCliente() {
        return cliente;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setDataExpiracao(LocalDate dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public void setLimite(double limite) {
        this.limite = limite;
    }

    public void setAvailableLimit(double availableLimit) {
        this.availableLimit = availableLimit;
    }

    public void setDataVencimentoFatura(Integer dataVencimentoFatura) {
        this.dataVencimentoFatura = dataVencimentoFatura;
    }

    public void setUltimaAlteracaoLimite(LocalDate ultimaAlteracaoLimite) {
        this.ultimaAlteracaoLimite = ultimaAlteracaoLimite;
    }

    public void setCliente(ClienteDomain cliente) {
        this.cliente = cliente;
    }
}
