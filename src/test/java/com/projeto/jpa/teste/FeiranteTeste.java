/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.projeto.jpa.teste;

import com.projeto.jpa.Fazenda;
import com.projeto.jpa.Feirante;
import com.projeto.jpa.Localizacao;
import com.projeto.jpa.Produto;
import com.projeto.jpa.TipoUsuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import java.util.Calendar;
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

/**
 *
 * @author Izavan
 */
public class FeiranteTeste {

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
    public void persistirFeirante() {
        Feirante feirante;
        feirante = criarFeirante();
        em.persist(feirante);
        em.flush();

        assertNotNull(feirante.getId());
    }

    @Test
    public void consultarFeirante() {
        Feirante feirante = em.find(Feirante.class, 1L);
        assertEquals("956.666.035-16", feirante.getCpf());
        assertEquals("feirante@gmail.com", feirante.getEmail());
        assertEquals("feirante", feirante.getLogin());
        assertEquals("feirante da silva", feirante.getNome());
        assertEquals("senha", feirante.getSenha());
        assertEquals("FEIRANTE", feirante.getTipo().toString());
        assertTrue(feirante.getProduto().size() == 1);

    }

    private Feirante criarFeirante() {

        Feirante feirante = new Feirante();

        feirante.setCpf("888.999.444-63");
        feirante.setLogin("usuario");
        feirante.setNome("Carlos Alberto");
        feirante.setEmail("feirante1@gmail.com");
        feirante.setSenha("123456");
        feirante.setTipo(TipoUsuario.FEIRANTE);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 1981);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 25);
        feirante.setDataNascimento(c.getTime());

        Query query = em.createQuery("SELECT p FROM Produto p");
        List<Produto> produtos = query.getResultList();
        produtos.forEach(produto -> {
            feirante.adicionar(produto);
        });
        
        return feirante;
    }
}
