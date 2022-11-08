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
@Table(name="TB_LEGUME") 
@DiscriminatorValue(value = "L")
@PrimaryKeyJoinColumn(name="ID_MERCADORIA", referencedColumnName = "ID")
public class Legume extends Mercadoria{
    
    @Column(name = "NUM_QUANTIDADE_LEGUME")
    private int quantidade;
    @Column(name = "TXT_DESCRICAO")
    private String descricao;
    @Column(name = "TXT_FERTILIZANTE")
    private String fertilizante;
    @Column(name = "TXT_TIPO_SOLO")
    private String tipoSolo;

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

    public String getFertilizante() {
        return fertilizante;
    }

    public void setFertilizante(String fertilizante) {
        this.fertilizante = fertilizante;
    }

    public String getTipoSolo() {
        return tipoSolo;
    }

    public void setTipoSolo(String tipoSolo) {
        this.tipoSolo = tipoSolo;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("com.projeto.jpa.Fruta[");
        sb.append(super.toString());
        sb.append(", ");
        sb.append(fertilizante);
        sb.append(", ");
        sb.append(tipoSolo);        
        sb.append("]");
        return sb.toString();
    }    
    
}
