/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.projeto.jpa.teste;

import com.projeto.jpa.Favorito;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hamcrest.CoreMatchers;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Izavan
 */
public class FavoritoJpqlTeste {
    
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
    public void favoritoPorNome() {
        logger.info("Executando favoritoPorNome()");
        TypedQuery<Favorito> query = em.createQuery(
                "SELECT f FROM Favorito f WHERE f.nome LIKE :nome",
                Favorito.class);
        query.setParameter("nome", "morango");
        List<Favorito> favoritos = query.getResultList();

        favoritos.forEach(favorito -> {
            assertTrue(favorito.getNome().startsWith("morango"));
        });

        assertEquals(1, favoritos.size());
    }
    
     @Test
    public void favoritosFruta() {
        logger.info("Executando favoritosFruta()");
        TypedQuery<Favorito> query;
        query = em.createQuery(
                "SELECT f FROM Favorito f WHERE f.tipo like ?1",
                Favorito.class);
        query.setParameter(1, "fruta"); //Setando parÃ¢metro posicional.
        query.setMaxResults(20); //Determinando quantidade mÃ¡xima de resultados.
        List<Favorito> favoritos = query.getResultList();

        favoritos.forEach(favorito -> {
            assertEquals("fruta", favorito.getTipo());
        });

        assertEquals(2, favoritos.size());
    }
    
    @Test
    public void favoritoFrutaLegume() {
        logger.info("Executando favoritoFrutaLegume()");
        TypedQuery<Favorito> query;
        query = em.createQuery(
                "SELECT f FROM Favorito f "
                + "WHERE f.tipo LIKE ?1 "
                + "OR f.tipo LIKE ?3",
                Favorito.class);
        query.setParameter(1, "fruta"); //Setando parÃ¢metro posicional.
        query.setParameter(3, "legume"); //Setando parÃ¢metro posicional.        
        List<Favorito> favoritos = query.getResultList();

        favoritos.forEach(favorito -> {
            assertThat(favorito.getTipo(),
                    CoreMatchers.anyOf(
                            startsWith("fruta"),
                            startsWith("legume")));
        });

        assertEquals(3, favoritos.size());
    }

}
