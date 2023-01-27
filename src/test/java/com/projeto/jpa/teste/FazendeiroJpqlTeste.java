/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.projeto.jpa.teste;

import com.projeto.jpa.Fazendeiro;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Izavan
 */
public class FazendeiroJpqlTeste {
    
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
    
       protected Date getData(Integer dia, Integer mes, Integer ano) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, ano);
        c.set(Calendar.MONTH, mes);
        c.set(Calendar.DAY_OF_MONTH, dia);
        return c.getTime();
    }

    @Test
    public void fazendeiroPorNome() {
        logger.info("Executando fazendeiroPorNome()");
        TypedQuery<Fazendeiro> query = em.createQuery(
                "SELECT f FROM Fazendeiro f WHERE f.nome LIKE :nome",
                Fazendeiro.class);
        query.setParameter("nome", "fazendeiro da silva");
        List<Fazendeiro> fazendeiros = query.getResultList();

        fazendeiros.forEach(fazendeiro -> {
            assertTrue(fazendeiro.getNome().startsWith("fazendeiro da silva"));
        });

        assertEquals(1, fazendeiros.size());
    }
    
    @Test
    public void fazendeirosPorDataNascimento() {
        logger.info("Executando fazendeirosPorDataNascimento()");
        TypedQuery<Fazendeiro> query;
        query = em.createQuery(
                "SELECT f FROM Fazendeiro f WHERE f.dataNascimento BETWEEN ?1 AND ?2",
                Fazendeiro.class);
        query.setParameter(1, getData(18, Calendar.FEBRUARY, 2001));
        query.setParameter(2, getData(26, Calendar.DECEMBER, 2006));
        List<Fazendeiro> fazendeiros = query.getResultList();
        assertEquals(2, fazendeiros.size());
    }
    
    @Test
    public void ordenacaoFazendeiro() {
        logger.info("Executando ordenacaoFazendeiro()");
        TypedQuery<Fazendeiro> query;
        query = em.createQuery(
                "SELECT f FROM Fazendeiro f ORDER BY f.nome DESC",
                Fazendeiro.class);
        List<Fazendeiro> fazendeiros = query.getResultList();
        assertEquals(2, fazendeiros.size());
        assertEquals("fazendeiro da silva", fazendeiros.get(0).getNome());
        assertEquals("Lucas Henrique", fazendeiros.get(1).getNome());
    }

}
