/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.projeto.jpa.teste;

import com.projeto.jpa.Fazendeiro;
import com.projeto.jpa.Feirante;
import com.projeto.jpa.Produto;
import com.projeto.jpa.TipoUsuario;
import static com.projeto.jpa.teste.FazendeiroTeste.logger;
import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        assertEquals("111.555.396-33", feirante.getCpf());
        assertEquals("feiranteemailnovo@gmail.com", feirante.getEmail());
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
    
    @Test
    public void atualizarFeirante() {
        logger.info("Executando atualizarFeirante()");
        String novoEmail = "feiranteemailnovo@gmail.com";
        String novoCpf = "111.555.396-33";
        Long id = 1L;
        Feirante feirante = em.find(Feirante.class, id);
        feirante.setEmail(novoEmail);
        feirante.setCpf(novoCpf);

        em.flush();
        String jpql = "SELECT f FROM  Feirante f WHERE f.id = ?1";
        TypedQuery<Feirante> query = em.createQuery(jpql,  Feirante.class);
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        query.setParameter(1, id);
        feirante = query.getSingleResult();
        assertEquals(novoEmail, feirante.getEmail());
        assertEquals(novoCpf, feirante.getCpf());
    }

    @Test
    public void atualizarFazendeiroMerge() {
        logger.info("Executando atualizarFeiranteMerge()");
        String novoEmail = "feirantedomerge2@gmail.com";
        String novoCpf = "454.983.588-37";
        Long id = 2L;
        Feirante feirante = em.find(Feirante.class, id);
        feirante.setEmail(novoEmail);
        feirante.setCpf(novoCpf);
        em.clear();
        em.merge(feirante);
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        feirante = em.find(Feirante.class, id, properties);
        assertEquals(novoEmail, feirante.getEmail());
        assertEquals(novoCpf, feirante.getCpf());
    }

   @Test
    public void removerFazendeiro() {
        logger.info("Executando removerFeirante()");
        Feirante feirante = em.find(Feirante.class, 3L);
        em.remove(feirante);
    }
}
