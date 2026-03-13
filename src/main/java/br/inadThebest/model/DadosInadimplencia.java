package br.inadThebest.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DadosInadimplencia {

    private String matriz;
    private String nomeParceiro;
    private String cliente;
    private String cpfCnpj;
    private String vendedor;
    private String emailVendedor;
    private Integer codigoTitulo;
    private LocalDate dataVencimento;
    private Integer numeroNota;
    private Integer diasVencido;
    private BigDecimal valor;

    // Getters e Setters

    public String getMatriz() {
        return matriz;
    }

    public void setMatriz(String matriz) {
        this.matriz = matriz;
    }

    public String getNomeParceiro() {
        return nomeParceiro;
    }

    public void setNomeParceiro(String nomeParceiro) {
        this.nomeParceiro = nomeParceiro;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public String getEmailVendedor() {
        return emailVendedor;
    }

    public void setEmailVendedor(String emailVendedor) {
        this.emailVendedor = emailVendedor;
    }

    public Integer getCodigoTitulo() {
        return codigoTitulo;
    }

    public void setCodigoTitulo(Integer codigoTitulo) {
        this.codigoTitulo = codigoTitulo;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Integer getNumeroNota() {
        return numeroNota;
    }

    public void setNumeroNota(Integer numeroNota) {
        this.numeroNota = numeroNota;
    }

    public Integer getDiasVencido() {
        return diasVencido;
    }

    public void setDiasVencido(Integer diasVencido) {
        this.diasVencido = diasVencido;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
