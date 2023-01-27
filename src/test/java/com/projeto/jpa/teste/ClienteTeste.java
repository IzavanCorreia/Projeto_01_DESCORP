/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.projeto.jpa.teste;

/**
 *
 * @author Izavan
 */
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
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteTeste {

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
    public void persistirCliente() {
        Cliente cliente = new Cliente();
        cliente.setNome("Eduardo");
        cliente.setEmail("cliente1@gmail.com");
        cliente.setLogin("cliente1");
        cliente.setSenha("teste");
        cliente.setCpf("534.585.764-45");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 1981);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 25);
        cliente.setDataNascimento(c.getTime());
        cliente.addContatos("(81) 98565-3621");
        cliente.addContatos("(81) 96905-8594");
        cliente.setTipo(TipoUsuario.CLIENTE);

        Favorito favorito = em.find(Favorito.class, 1L);
        favorito.adicionar(cliente);
        assertNotNull(favorito);

        em.persist(cliente);
        em.flush();
        assertNotNull(cliente.getId());
    }

    @Test
    public void consultarCliente() {
        Cliente cliente = em.find(Cliente.class, 1L);
        assertNotNull(cliente);
        assertEquals("888.754.698-63", cliente.getCpf());
        assertEquals("teste", cliente.getLogin());
        assertEquals("Carlos", cliente.getNome());
        assertEquals("emailnovo@gmail.com", cliente.getEmail());
        assertEquals("teste", cliente.getSenha());
        assertEquals("CLIENTE", cliente.getTipo().toString());
        assertEquals(2, cliente.getContatos().size());
        assertTrue(cliente.getContatos().contains("(81) 98963-2154"));

        Favorito favorito = cliente.getFavorito();
        assertNotNull(favorito);
        assertEquals("morango", favorito.getNome());
        assertEquals("morango muito azedo", favorito.getDescricao());
        assertEquals("fruta", favorito.getTipo());
    }

    @Test
    public void atualizarCliente() {
        logger.info("Executando atualizarCliente()");
        String novoEmail = "emailnovo@gmail.com";
        String novoCpf = "888.754.698-63";
        String novoTelefone = "(81) 98963-2154";
        Long id = 1L;
        Cliente cliente = em.find(Cliente.class, id);
        cliente.setEmail(novoEmail);
        cliente.setCpf(novoCpf);
        cliente.addContatos(novoTelefone);

        em.flush();
        String jpql = "SELECT c FROM Cliente c WHERE c.id = ?1";
        TypedQuery<Cliente> query = em.createQuery(jpql, Cliente.class);
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        query.setParameter(1, id);
        cliente = query.getSingleResult();
        assertEquals(novoEmail, cliente.getEmail());
        assertEquals(novoCpf, cliente.getCpf());
        assertTrue(cliente.getContatos().contains(novoTelefone));
    }

    @Test
    public void atualizarClienteMerge() {
        logger.info("Executando atualizarClienteMerge()");
        String novoEmail = "fulano_de_tal2@gmail.com";
        String telefone = "(81) 990901010";
        Long id = 2L;
        Cliente cliente = em.find(Cliente.class, id);
        cliente.setEmail(novoEmail);
        cliente.addContatos(telefone);
        em.clear();
        em.merge(cliente);
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        cliente = em.find(Cliente.class, id, properties);
        assertEquals(novoEmail, cliente.getEmail());
        assertTrue(cliente.getContatos().contains(telefone));
    }

    @Test
    public void removerCliente() {
        logger.info("Executando removerCliente()");
        Cliente cliente = em.find(Cliente.class, 3L);
        em.remove(cliente);
        Favorito favorito = em.find(Favorito.class, 1L);      
        assertNotNull(favorito);
    }

}
