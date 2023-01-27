/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.projeto.jpa.teste;

import com.projeto.jpa.Fruta;
import com.projeto.jpa.Mercadoria;
import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertNull;

/**
 *
 * @author Izavan
 */
public class FrutaTeste {

    private static EntityManagerFactory emf;
    protected static Logger logger;
    private static EntityManager em;
    private EntityTransaction et;

    @BeforeClass
    public static void setUpClass() {
        logger = Logger.getGlobal();
        logger.setLevel(Level.INFO);
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
        assertEquals("Graviola para sorvetes e sucos naturais", fruta.getDescricao());
        assertEquals("Muito maduro", fruta.getMaturacao());
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
    
    @Test
    public void atualizarFruta() {
        logger.info("Executando atualizarFruta()");
        String novaDescricao = "Graviola para sorvetes e sucos naturais";
        String novaMaturacao = "Muito maduro";
        Long id = 1L;
        Fruta fruta = em.find(Fruta.class, id);
        fruta.setDescricao(novaDescricao);
        fruta.setMaturacao(novaMaturacao);
        
        em.flush();
        String jpql = "SELECT f FROM Fruta f WHERE f.id = ?1";
        TypedQuery<Fruta> query = em.createQuery(jpql, Fruta.class);
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        query.setParameter(1, id);
        fruta = query.getSingleResult();
        assertEquals(novaDescricao, fruta.getDescricao());
        assertEquals(novaMaturacao, fruta.getMaturacao());
    }

    @Test
    public void atualizarFrutaMerge() {
        logger.info("Executando atualizarFrutaMerge()");
        String novaDescricao = "Rico em vitaminas e sais minerais";
        String novaMaturacao = "Verde";
        Long id = 2L;
        Fruta fruta = em.find(Fruta.class, id);
        fruta.setDescricao(novaDescricao);
        fruta.setMaturacao(novaMaturacao);
        em.clear();
        em.merge(fruta);
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        fruta = em.find(Fruta.class, id, properties);
        assertEquals(novaDescricao, fruta.getDescricao());
        assertEquals(novaMaturacao, fruta.getMaturacao());
    }

    @Test
    public void removerMercadoria() {
        logger.info("Executando removerMercadoria()");
        Mercadoria mercadoria = em.find(Mercadoria.class, 5L);
        em.remove(mercadoria);
        
        Fruta fruta = em.find(Fruta.class, 5L);
        assertNull(fruta);
    }
}
