/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.projeto.jpa.teste;

import com.projeto.jpa.Fazenda;
import com.projeto.jpa.Fazendeiro;
import com.projeto.jpa.Localizacao;
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
public class FazendeiroTeste {

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
    public void persistirFazendeiro() {
        Fazendeiro fazendeiro;
        fazendeiro = criarFazendeiro();
        em.persist(fazendeiro);
        em.flush(); //forÃ§a que a persistÃªncia realizada vÃ¡ para o banco neste momento.

        assertNotNull(fazendeiro.getId());
        assertNotNull(fazendeiro.getFazenda().getId());

    }

    @Test
    public void consultarFazendeiro() {
        Fazendeiro fazendeiro = em.find(Fazendeiro.class, 1L);
        assertEquals("808.257.284-10", fazendeiro.getCpf());
        assertEquals("fazendeiro@gmail.com", fazendeiro.getEmail());
        assertEquals("fazendeiro", fazendeiro.getLogin());
        assertEquals("fazendeiro da silva", fazendeiro.getNome());
        assertEquals("senha", fazendeiro.getSenha());
        
        assertEquals("FAZENDEIRO", fazendeiro.getTipo().toString());

        Fazenda fazenda = fazendeiro.getFazenda();
        assertNotNull(fazenda);
        assertEquals("Fazenda dia feliz", fazenda.getNome());
        assertEquals("222.323.233-15", fazenda.getCnpj());
        assertEquals("300", fazenda.getQtdEquitares());
    }

    private Fazendeiro criarFazendeiro() {
        Fazendeiro fazendeiro = new Fazendeiro();
        fazendeiro.setNome("José Fazendeiro");
        fazendeiro.setEmail("josefazedeiro@gmail.com");
        fazendeiro.setLogin("jose");
        fazendeiro.setSenha("user");
        fazendeiro.setCpf("118.729.500-00");
        fazendeiro.setTipo(TipoUsuario.FAZENDEIRO);
     
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2006);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 26);
        fazendeiro.setDataNascimento(c.getTime());
 
        criarFazenda(fazendeiro);
        return fazendeiro;
    }
    
    private void criarFazenda(Fazendeiro fazendeiro) {
        Fazenda fazenda = new Fazenda();
        fazenda.setNome("Fazenda campo verde");
        fazenda.setCnpj("876.789.654-67");
        fazenda.setQtdEquitares("350");
        criarLocalizacao(fazenda);
        fazendeiro.setFazenda(fazenda);     
    }
    
    public static void criarLocalizacao(Fazenda fazenda) {
        Localizacao localizacao = new Localizacao();
        localizacao.setLogradouro("Rua Alcantara Machado");
        localizacao.setBairro("Vasco da Gama");
        localizacao.setCidade("Recife");
        localizacao.setEstado("Pernambuco");
        localizacao.setCep("52081-495");
        localizacao.setNumero(384);
        fazenda.setLocalizacao(localizacao);
    }
    
    @Test
    public void atualizarFazendeiro() {
        logger.info("Executando atualizarFazendeiro()");
        String novoEmail = "fazendeiroemailnovo@gmail.com";
        String novoCpf = "985.766.258-13";
        Long id = 1L;
        Fazendeiro fazendeiro = em.find( Fazendeiro.class, id);
        fazendeiro.setEmail(novoEmail);
        fazendeiro.setCpf(novoCpf);

        em.flush();
        String jpql = "SELECT c FROM  Fazendeiro c WHERE c.id = ?1";
        TypedQuery<Fazendeiro> query = em.createQuery(jpql,  Fazendeiro.class);
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        query.setParameter(1, id);
        fazendeiro = query.getSingleResult();
        assertEquals(novoEmail, fazendeiro.getEmail());
        assertEquals(novoCpf, fazendeiro.getCpf());
    }

    @Test
    public void atualizarFazendeiroMerge() {
        logger.info("Executando atualizarFazendeiroMerge()");
        String novoEmail = "fazendeirodomerger2@gmail.com";
        String novoCpf = "874.333.212-44";
        Long id = 2L;
        Fazendeiro fazendeiro = em.find(Fazendeiro.class, id);
        fazendeiro.setEmail(novoEmail);
        fazendeiro.setCpf(novoCpf);
        em.clear();
        em.merge(fazendeiro);
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        fazendeiro = em.find(Fazendeiro.class, id, properties);
        assertEquals(novoEmail, fazendeiro.getEmail());
        assertEquals(novoCpf, fazendeiro.getCpf());
    }

   @Test
    public void removerFazendeiro() {
        logger.info("Executando removerFazendeiro()");
        Fazendeiro fazendeiro = em.find(Fazendeiro.class, 3L);
        em.remove(fazendeiro);
    }

}
