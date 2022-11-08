/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.projeto.jpa;


/**
 *
 * @author Izavan
 */

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/*
 * Todos os campos de Endereco serÃ£o armazenados na mesma tabela 
 * que armazena os dados de Usuario.
 */
@Embeddable
public class Localizacao {
    
    @Column(name = "END_TXT_LOGRADOURO", length = 150, nullable = false)
    private String logradouro;
    @Column(name = "END_TXT_BAIRRO", length = 150, nullable = false)
    private String bairro;
    @Column(name = "END_NUMERO", length = 5, nullable = false)
    private Integer numero;
    @Column(name = "END_TXT_CEP", length = 20, nullable = false)
    private String cep;
    @Column(name = "END_TXT_CIDADE", length = 50, nullable = false)
    private String cidade;
    @Column(name = "END_TXT_ESTADO", length = 50, nullable = false)
    private String estado;

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }
    
    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
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
}
