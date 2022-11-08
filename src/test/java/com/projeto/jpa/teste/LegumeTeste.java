/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.projeto.jpa.teste;

import com.projeto.jpa.Legume;
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
public class LegumeTeste {
    
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
    public void persistirLegume() {
        Legume legume = criarLegume();
        em.persist(legume);
        em.flush();

        assertNotNull(legume.getId());
    }

    @Test
    public void consultarLegume() {
        Legume legume = em.find(Legume.class, 3L);
        assertNotNull(legume);
        assertEquals("alface novinho",legume.getDescricao());
        assertEquals("sem fertilizante", legume.getFertilizante());
        assertEquals("solo rico em ferro", legume.getTipoSolo());
    }

    private Legume criarLegume() {
        Legume legume = new Legume();
        legume.setNome("rúcula");
        legume.setCodigo("003");
        legume.setTipo("com agrotoxico");
        legume.setValidade("40 dias");
        legume.setQuantidade(300);
        legume.setDescricao("muito gostoso e fresquinho");
        legume.setFertilizante("usa fertilizante para controle de insetos");
        legume.setTipoSolo("solo molhado e rico em matéria orgânica");    
        return legume;
    }
}
