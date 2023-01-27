/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.projeto.jpa.teste;

import com.projeto.jpa.Cliente;
import com.projeto.jpa.Favorito;
import com.projeto.jpa.TipoUsuario;
import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Izavan
 */
public class FavoritoTeste {

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
    public void persistirFavorito() {
        Favorito favorito;
        favorito = criarFavorito();
        em.persist(favorito);
        em.flush(); //forÃ§a que a persistÃªncia realizada vÃ¡ para o banco neste momento.

        assertNotNull(favorito.getId());

    }

    @Test
    public void consultarFavorito() {
        Favorito favorito = em.find(Favorito.class, 1L);
        assertEquals("morango", favorito.getNome());
        assertEquals("morango muito azedo", favorito.getDescricao());
        assertEquals("fruta", favorito.getTipo());
    }

    private Favorito criarFavorito() {
        Favorito favorito = new Favorito();
        Cliente cliente1 = new Cliente();
        
        favorito.setNome("banana");
        favorito.setDescricao("banana verde");
        favorito.setTipo("fruta");

        cliente1.setNome("Eduardo");
        cliente1.setEmail("cliente1@gmail.com");
        cliente1.setLogin("cliente1");
        cliente1.setSenha("teste");
        cliente1.setCpf("534.585.764-45");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 1981);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 25);
        cliente1.setDataNascimento(c.getTime());
        cliente1.addContatos("(81) 98565-3621");
        cliente1.addContatos("(81) 96905-3356");
        cliente1.setTipo(TipoUsuario.CLIENTE);

        favorito.adicionar(cliente1);
        
        return favorito;
    }
    
    @Test
    public void atualizarFavorito() {
        logger.info("Executando atualizarFavorito()");
        String novoNome = "Morango Verde";
        String novoTipo = "Fruta tropical";
        Long id = 1L;
        Favorito favorito = em.find(Favorito.class, id);
        favorito.setNome(novoNome);
        favorito.setTipo(novoTipo);
        
        em.flush();
        String jpql = "SELECT f FROM Favorito f WHERE f.id = ?1";
        TypedQuery<Favorito> query = em.createQuery(jpql, Favorito.class);
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        query.setParameter(1, id);
        favorito = query.getSingleResult();
        assertEquals(novoNome, favorito.getNome());
        assertEquals(novoTipo, favorito.getTipo());
    }

    @Test
    public void atualizarFavoritoMerge() {
        logger.info("Executando atualizarFavoritoMerge()");
        String novoNome = "Uva verde";
        String novoTipo = "Fruta doce";
        Long id = 2L;
        Favorito favorito = em.find(Favorito.class, id);
        favorito.setNome(novoNome);
        favorito.setTipo(novoTipo);
        em.clear();
        em.merge(favorito);
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        favorito = em.find(Favorito.class, id, properties);
        assertEquals(novoNome, favorito.getNome());
        assertEquals(novoTipo, favorito.getTipo());
    }

    @Test
    public void removerFavorito() {
        logger.info("Executando removerFavorito()");
        Favorito favorito = em.find(Favorito.class, 3L);
        em.remove(favorito);
    }
}
