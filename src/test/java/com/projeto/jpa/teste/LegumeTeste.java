/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.projeto.jpa.teste;

import com.projeto.jpa.Legume;
import com.projeto.jpa.Mercadoria;
import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Izavan
 */
public class LegumeTeste {
    
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

    @Test
    public void atualizarLegume() {
        logger.info("Executando atualizarLegume()");
        String novaDescricao = "Alface muito verde e novo para consumo";
        String novoFertilizante = "Utiliza fertilizantes naturais";
        Long id = 3L;
        Legume legume = em.find(Legume.class, id);
        legume.setDescricao(novaDescricao);
        legume.setFertilizante(novoFertilizante);
        
        em.flush();
        String jpql = "SELECT l FROM Legume l WHERE l.id = ?1";
        TypedQuery<Legume> query = em.createQuery(jpql, Legume.class);
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        query.setParameter(1, id);
        legume = query.getSingleResult();
        assertEquals(novaDescricao, legume.getDescricao());
        assertEquals(novoFertilizante, legume.getFertilizante());
    }

    @Test
    public void atualizarLegumeMerge() {
        logger.info("Executando atualizarLegumeMerge()");
        String novaDescricao = "Espinafre muito gostoso";
        String novoFertilizante = "Sem nenhum fertilizante";
        Long id = 4L;
        Legume legume = em.find(Legume.class, id);
        legume.setDescricao(novaDescricao);
        legume.setFertilizante(novoFertilizante);
        em.clear();
        em.merge(legume);
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        legume = em.find(Legume.class, id, properties);
        assertEquals(novaDescricao, legume.getDescricao());
        assertEquals(novoFertilizante, legume.getFertilizante());
    }

    @Test
    public void removerMercadoria() {
        logger.info("Executando removerMercadoria()");
        Mercadoria mercadoria = em.find(Mercadoria.class, 5L);
        em.remove(mercadoria);
        
        Legume legume = em.find(Legume.class, 5L);
        assertNull(legume);
    }
}
