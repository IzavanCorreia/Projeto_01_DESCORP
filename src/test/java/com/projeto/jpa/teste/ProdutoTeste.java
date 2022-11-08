/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.projeto.jpa.teste;

import com.projeto.jpa.Feirante;
import com.projeto.jpa.Produto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Izavan
 */
public class ProdutoTeste {

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
}
