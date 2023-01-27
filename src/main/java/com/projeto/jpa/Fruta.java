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
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name="TB_FRUTA") 
@DiscriminatorValue(value = "F")
@PrimaryKeyJoinColumn(name="ID_MERCADORIA", referencedColumnName = "ID")
public class Fruta extends Mercadoria{
    
    @Column(name = "NUM_QUANTIDADE_FRUTA")
    private int quantidade;
    @Column(name = "TXT_DESCRICAO")
    private String descricao;
    @Column(name = "TXT_MATURACAO")
    private String maturacao;

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMaturacao() {
        return maturacao;
    }

    public void setMaturacao(String maturacao) {
        this.maturacao = maturacao;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("com.projeto.jpa.Fruta[");
        sb.append(super.toString());
        sb.append(", ");
        sb.append(quantidade);
        sb.append(", ");
        sb.append(descricao);        
        sb.append("]");
        return sb.toString();
    }    

    
}
