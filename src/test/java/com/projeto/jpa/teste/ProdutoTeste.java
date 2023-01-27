/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.projeto.jpa.teste;

import com.projeto.jpa.Feirante;
import com.projeto.jpa.Produto;
import static com.projeto.jpa.teste.FrutaTeste.logger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import jakarta.persistence.CacheRetrieveMode;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Izavan
 */
public class ProdutoTeste {

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
    public void persistirProduto() {
        Produto produto;
        produto = criarProduto();
        em.persist(produto);
        em.flush(); //forÃ§a que a persistÃªncia realizada vÃ¡ para o banco neste momento.

        assertNotNull(produto.getId());

    }

    @Test
    public void consultarProduto() {
        Produto produto = em.find(Produto.class, 1L);
        assertEquals("morango", produto.getNome());
        assertEquals("006", produto.getCodigo());
        assertEquals("fruta", produto.getTipo());
        assertEquals("8 dias", produto.getValidade());
        assertTrue(produto.getFeirantes().size() == 1);
    }

    private Produto criarProduto() {
        Produto produto = new Produto();
        produto.setNome("Manga");
        produto.setCodigo("001");
        produto.setTipo("Sem agrotóxico");
        produto.setValidade("30 dias");

        Query query = em.createQuery("SELECT f FROM Feirante f");
        List<Feirante> feirantes = query.getResultList();
        feirantes.forEach(feirante -> {
            produto.adicionar(feirante);
        });

        return produto;
    }

    @Test
    public void atualizarProduto() {
        logger.info("Executando atualizarProduto()");
        String novoNome = "Acerola";
        String novoTipo = "fruta";
        Long id = 1L;
        Produto produto = em.find(Produto.class, id);
        produto.setNome(novoNome);
        produto.setTipo(novoTipo);

        em.flush();
        String jpql = "SELECT p FROM Produto p WHERE p.id = ?1";
        TypedQuery<Produto> query = em.createQuery(jpql, Produto.class);
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        query.setParameter(1, id);
        produto = query.getSingleResult();
        assertEquals(novoNome, produto.getNome());
        assertEquals(novoTipo, produto.getTipo());
    }

    @Test
    public void atualizarProdutoMerge() {
        logger.info("Executando atualizarProdutoMerge()");
        String novoNome = "Beringela";
        String novoTipo = "legume";
        Long id = 2L;
        Produto produto = em.find(Produto.class, id);
        produto.setNome(novoNome);
        produto.setTipo(novoTipo);
        em.clear();
        em.merge(produto);
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        produto = em.find(Produto.class, id, properties);
        assertEquals(novoNome, produto.getNome());
        assertEquals(novoTipo, produto.getTipo());
    }

    @Test
    public void removerProduto() {
        logger.info("Executando removerProduto()");
        Produto produto = em.find(Produto.class, 3L);
        em.remove(produto);

    }
}
