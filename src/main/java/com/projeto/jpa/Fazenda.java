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
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "TB_FAZENDA")
public class Fazenda {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;  
    @OneToOne(mappedBy = "fazenda", optional = false, fetch = FetchType.LAZY)
    private Fazendeiro fazendeiro;
    @Column(name = "TXT_CNPJ")
    private String cnpj;
    @Column(name = "TXT_NOME")
    private String nome;
    @Column (name = "TXT_QTD_EQUITARES")
    private String qtdEquitares;
    @Embedded
    private Localizacao localizacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Fazendeiro getFazendeiro() {
        return fazendeiro;
    }

    public void setFazendeiro(Fazendeiro fazendeiro) {
        this.fazendeiro = fazendeiro;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getQtdEquitares() {
        return qtdEquitares;
    }

    public void setQtdEquitares(String qtdEquitares) {
        this.qtdEquitares = qtdEquitares;
    }

    public Localizacao getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Fazenda)) {
            return false;
        }
        Fazenda other = (Fazenda) object;

        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("com.projeto.jpa.Fazenda[");
        sb.append(this.id);
        sb.append(", ");
        sb.append(this.nome);        
        sb.append(", ");
        sb.append(this.cnpj);
        
        sb.append("]");
        return sb.toString();
    }
}
