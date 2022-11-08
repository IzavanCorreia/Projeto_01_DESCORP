/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.projeto.jpa.teste;

import com.projeto.jpa.Fruta;
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
 * @author Izavan
 */
public class FrutaTeste {

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
    public void persistirFruta() {
        Fruta fruta = criarFruta();
        em.persist(fruta);
        em.flush();

        assertNotNull(fruta.getId());
    }

    @Test
    public void consultarFruta() {
        Fruta fruta = em.find(Fruta.class, 1L);
        assertNotNull(fruta);
        assertEquals("graviola para sucos", fruta.getDescricao());
        assertEquals("maduro", fruta.getMaturacao());
    }

    private Fruta criarFruta() {
        Fruta fruta = new Fruta();
        fruta.setNome("morango");
        fruta.setCodigo("004");
        fruta.setTipo("com agrotoxico");
        fruta.setValidade("10 dias");
        fruta.setQuantidade(500);
        fruta.setDescricao("muito doce");
        fruta.setMaturacao("bem maduros");
        return fruta;
    }
}
