/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.projeto.jpa.teste;

import com.projeto.jpa.Fazenda;
import com.projeto.jpa.Localizacao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Irwin
 */
public class FazendaTeste {

    private static EntityManagerFactory emf;
    private static EntityManager em;
    private EntityTransaction et;

    @BeforeClass
    public static void setUpClass() {
        emf = Persistence.createEntityManagerFactory("Projeto_01");
        DbUnitUtil.inserirDados();
    }

    @AfterClass
    public static void tearDownClass() {
        emf.close();
    }

    @Before
    public void setUp() {
        em = emf.createEntityManager();
        et = em.getTransaction();
        et.begin();
    }

    @After
    public void tearDown() {
        if (!et.getRollbackOnly()) {
            et.commit();
        }
        em.close();
    }

    @Test
    public void persistirFazenda() {
        Fazenda fazenda;
        fazenda = criarFazenda();
        em.persist(fazenda);
        em.flush();

        assertNotNull(fazenda.getId());
    }
    

    @Test
    public void consultarFazenda() {
        Fazenda fazenda = em.find(Fazenda.class, 1L);
        assertEquals("222.323.233-15", fazenda.getCnpj());
        assertEquals("Fazenda dia feliz", fazenda.getNome());
        assertEquals("300", fazenda.getQtdEquitares());

        Localizacao localizacao = fazenda.getLocalizacao();
        assertNotNull(localizacao);
        assertEquals("Rua Nossa Senhora do Carmo", localizacao.getLogradouro());
        assertEquals("Cajueiro Seco", localizacao.getBairro());
        assertEquals("Recife", localizacao.getCidade());
        assertEquals("Pernambuco", localizacao.getEstado());
        assertEquals("54330-220", localizacao.getCep());
    }

    private Fazenda criarFazenda() {
        Fazenda fazenda = new Fazenda();
        fazenda.setNome("Fazenda Corte Rapido");
        fazenda.setCnpj("222.888.765-98");
        fazenda.setQtdEquitares("560");
        
        criarLocalizacao(fazenda);
        return fazenda;
    }

    public static void criarLocalizacao(Fazenda fazenda) {
        Localizacao localizacao = new Localizacao();
        localizacao.setLogradouro("Rua Nosa Senhora da Piedade");
        localizacao.setBairro("Floriano");
        localizacao.setCidade("Jaboatão dos Guararapes");
        localizacao.setEstado("Pernambuco");
        localizacao.setCep("52678-500");
        localizacao.setNumero(400);
        fazenda.setLocalizacao(localizacao);
    }
     
}
