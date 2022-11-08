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
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.Calendar;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ClienteTeste {

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
        cliente.addContatos("(81) 96905-3356");
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
        assertEquals("444.323.233-15", cliente.getCpf());
        assertEquals("teste", cliente.getLogin());
        assertEquals("Carlos", cliente.getNome());
        assertEquals("teste@gmail.com", cliente.getEmail());
        assertEquals("teste", cliente.getSenha());
        assertEquals("CLIENTE", cliente.getTipo().toString());
        assertEquals(1, cliente.getContatos().size());
        assertTrue(cliente.getContatos().contains("(81) 96905-3356"));

        Favorito favorito = cliente.getFavorito();
        assertNotNull(favorito);
        assertEquals("morango", favorito.getNome());
        assertEquals("morango muito azedo", favorito.getDescricao());
        assertEquals("fruta", favorito.getTipo());
    }

}
