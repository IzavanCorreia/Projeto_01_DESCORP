/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.projeto.jpa;

/**
 *
 * @author Izavan
 */

import jakarta.persistence.CollectionTable;
import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Collection;
import java.util.HashSet;

@Entity
@Table(name = "TB_CLIENTE")
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_FAVORITO", referencedColumnName = "ID")
    private Favorito favorito;
    @Column(name = "TXT_CPF", nullable = false, length = 14, unique = true)
    private String cpf;
    @Column(name = "TXT_LOGIN", nullable = false, length = 50, unique = true)
    private String login;
    @Column(name = "TXT_NOME", nullable = false, length = 255)
    private String nome;
    @Column(name = "TXT_EMAIL", nullable = false, length = 50)
    private String email;
    @Column(name = "TXT_SENHA", nullable = false, length = 20)
    private String senha;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_NASCIMENTO", nullable = true)
    private Date dataNascimento;
    @ElementCollection 
    @CollectionTable(name = "TB_CONTATOS", 
            joinColumns = @JoinColumn(name = "ID_CLIENTE", nullable = false))
    @Column(name = "TXT_NUM_CONTATOS", nullable = false, length = 20)
    private Collection<String> contatos;
    @Enumerated(EnumType.STRING) //Use EnumType.ORDINAL para armazenar a enumeraÃ§Ã£o como inteiro.
    @Column(name = "TXT_TIPO_USUARIO", nullable = false, length = 20)
    private TipoUsuario tipo;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Favorito getFavorito() {
        return favorito;
    }

    public void setFavorito(Favorito favorito) {
        this.favorito = favorito;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
    
     public TipoUsuario getTipo() {
        return tipo;
    }

    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }
    
     public Collection<String> getContatos() {
        return contatos;
    }

    public void addContatos(String contato) {
        if (contatos == null) {
            contatos = new HashSet<>();
        }
        contatos.add(contato);
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Cliente)) {
            return false;
        }
        Cliente other = (Cliente) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("com.projeto.jpa.Cliente[");
        sb.append(this.id);
        sb.append(", ");
        sb.append(this.nome);        
        sb.append(", ");
        sb.append(this.login);
        sb.append(", ");
        sb.append(this.cpf);
        
        this.contatos.forEach(contato -> {
            sb.append(", ");
            sb.append(contato);
        });
        
        sb.append("]");
        return sb.toString();
    
 }
}
