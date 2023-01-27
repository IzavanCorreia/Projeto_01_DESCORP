/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.projeto.jpa.teste;

import com.projeto.jpa.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class ClienteJpqlTeste {

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
    public void clientePorNome() {
        logger.info("Executando clientePorNome()");
        TypedQuery<Cliente> query = em.createQuery(
                "SELECT c FROM Cliente c WHERE c.nome LIKE :nome",
                Cliente.class);
        query.setParameter("nome", "Carlos");
        List<Cliente> clientes = query.getResultList();

        clientes.forEach(cliente -> {
            assertTrue(cliente.getNome().startsWith("Carlos"));
        });

        assertEquals(1, clientes.size());
    }

    @Test
    public void clientePorNomeNamedQuery() {
        logger.info("Executando clientePorNomeNamedQuery()");
        TypedQuery<Cliente> query = em.createNamedQuery("Cliente.PorNome", Cliente.class);
        query.setParameter("nome", "Roberto Carlos");
        List<Cliente> clientes = query.getResultList();

        clientes.forEach(cliente -> {
            assertTrue(cliente.getNome().startsWith("Roberto Carlos"));
        });

        assertEquals(1, clientes.size());
    }
    
     @Test
    public void maximaEMinimaDataNascimento() {
        logger.info("Executando maximaEMinimaDataNascimento()");
        Query query = em.createQuery(
                "SELECT MAX(c.dataNascimento), MIN(c.dataNascimento) FROM Cliente c");
        Object[] resultado = (Object[]) query.getSingleResult();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String maiorData = dateFormat.format((Date) resultado[0]);
        String menorData = dateFormat.format((Date) resultado[1]);
        assertEquals("24-08-1999", maiorData);
        assertEquals("25-02-1981", menorData);
    }


}
