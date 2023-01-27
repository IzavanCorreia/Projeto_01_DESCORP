/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.projeto.jpa.teste;

import com.projeto.jpa.Fazenda;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hamcrest.CoreMatchers;
import static org.hamcrest.core.StringStartsWith.startsWith;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 *
 * @author Izavan
 */
public class FazendaJpqlTeste {

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
    public void fazendaPorNome() {
        logger.info("Executando fazendaPorNome()");
        TypedQuery<Fazenda> query = em.createQuery(
                "SELECT f FROM Fazenda f WHERE f.nome LIKE :nome",
                Fazenda.class);
        query.setParameter("nome", "Fazenda dia feliz");
        List<Fazenda> fazendas = query.getResultList();

        fazendas.forEach(fazenda -> {
            assertTrue(fazenda.getNome().startsWith("Fazenda dia feliz"));
        });

        assertEquals(1, fazendas.size());
    }

    @Test
    public void fazendaReciceJaboatao() {
        logger.info("Executando fazendaReciceJaboatao()");
        TypedQuery<Fazenda> query;
        query = em.createQuery(
                "SELECT f FROM Fazenda f "
                + "WHERE f.localizacao.cidade IN ('Recife', 'Jaboatão dos Guararapes')",
                Fazenda.class);
        List<Fazenda> fazendas = query.getResultList();

        fazendas.forEach(fazenda -> {
            assertThat(fazenda.getLocalizacao().getCidade(),
                    CoreMatchers.anyOf(
                            startsWith("Recife"),
                            startsWith("Jaboatão dos Guararapes")));
        });

        assertEquals(2, fazendas.size());
    }

    @Test
    public void localizacaoDistintas() {
        logger.info("Executando localizacaoDistintas()");
        TypedQuery<String> query
                = em.createQuery("SELECT DISTINCT(f.localizacao.bairro) FROM Fazenda f ORDER BY f.localizacao.bairro", String.class);
        List<String> bairros = query.getResultList();
        assertEquals(2, bairros.size());

    }
}
