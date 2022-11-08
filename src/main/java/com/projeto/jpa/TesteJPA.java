/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.projeto.jpa;

/**
 *
 * @author Izavan
 */
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TesteJPA {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Projeto_01");

    static {
        Logger.getGlobal().setLevel(Level.INFO);
    }

    public static void main(String[] args) {
        try {
            Long id = inserirFazendeiro();
            consultarFazendeiro(id);
            criarFavorito();
            criarProdutos();
            criarFeirantes();
            inserirMercadoria();
             //consultarMercadoria(id);
        } finally {
            emf.close();
        }
    }
    
    private static void consultarMercadoria(Long id) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            System.out.println("Consultando mercadoria na base...");
            Mercadoria mercadoria = em.find(Mercadoria.class, id);
            System.out.println("Imprimindo mercorias...");
            System.out.println(mercadoria.toString());
        } finally {
            if (em != null) {
                em.close();
            }            
        }
    }
    
     public static Long inserirMercadoria() {
        Mercadoria fruta = criarFruta();
        Mercadoria legume = criarLegume();
        
        EntityManager em = null;
        EntityTransaction et = null;
        try {
            em = emf.createEntityManager();
            et = em.getTransaction();
            et.begin();
            em.persist(fruta);
            em.persist(legume);
            et.commit();
        } catch (Exception ex) {
            if (et != null && et.isActive()) {
                Logger.getGlobal().log(Level.SEVERE,
                        "Cancelando transaÃ§Ã£o com erro. Mensagem: {0}", ex.getMessage());
                et.rollback();
                Logger.getGlobal().info("TransaÃ§Ã£o cancelada.");
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        
        return fruta.getId();
    }
    
     private static Fruta criarFruta() {
        Fruta fruta = new Fruta();
        fruta.setNome("Morango");
        fruta.setCodigo("004");
        fruta.setTipo("Com agrotoxico");
        fruta.setValidade("10 dias");
        fruta.setQuantidade(500);
        fruta.setDescricao("Muito doce");
        fruta.setMaturacao("Bem maduros"); 
        return fruta;
    }
    
    private static Legume criarLegume() {
        Legume legume = new Legume();
        legume.setNome("Alface");
        legume.setCodigo("003");
        legume.setTipo("Com agrotoxico");
        legume.setValidade("40 dias");
        legume.setQuantidade(300);
        legume.setDescricao("Muito gostoso e retirado na hora");
        legume.setFertilizante("Usa fertilizante para controle de insetos");
        legume.setTipoSolo("Solo molhado");    
        return legume;
    }
     

    public static void criarProdutos() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        Produto produto1 = new Produto();
        produto1.setNome("Manga");
        produto1.setCodigo("001");
        produto1.setTipo("Sem agrotóxico");
        produto1.setValidade("30 dias");
        Produto produto2 = new Produto();
        produto2.setNome("Milho");
        produto2.setCodigo("002");
        produto2.setTipo("Sem agrotóxico");
        produto2.setValidade("120 dias");

        try {
            et.begin();
            em.persist(produto1);
            em.persist(produto2);
            et.commit();
        } catch (Exception ex) {
            if (et != null && et.isActive()) {
                Logger.getGlobal().log(Level.SEVERE,
                        "Cancelando transaÃ§Ã£o com erro. Mensagem: {0}", ex.getMessage());
                et.rollback();
                Logger.getGlobal().info("TransaÃ§Ã£o cancelada.");
            }
        } finally {
            em.close();
        }
    }

    private static void criarFeirantes() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        Feirante feirante = new Feirante();

        try {
            et.begin();
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

            em.persist(feirante);
            em.flush(); //forÃ§a as alteraÃ§Ãµes a irem para o banco
            et.commit();
        } catch (Exception ex) {
            if (et != null && et.isActive()) {
                Logger.getGlobal().log(Level.SEVERE,
                        "Cancelando transaÃ§Ã£o com erro. Mensagem: {0}", ex.getMessage());
                et.rollback();
                Logger.getGlobal().info("TransaÃ§Ã£o cancelada.");
            }
        } finally {
            em.close();
        }
    }

    private static void consultarFazendeiro(Long id) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            Map<String, Object> properties = new HashMap<>();

            properties.put("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            System.out.println("Consultando fazendeiro na base...");
            Fazendeiro fazendeiro = em.find(Fazendeiro.class, id, properties);
            System.out.println("Imprimindo fazendeiro...");
            System.out.println(fazendeiro);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public static Long inserirFazendeiro() {
        Fazendeiro fazendeiro = criarFazendeiro();
        Fazenda fazenda = criarFazenda();
        fazendeiro.setFazenda(fazenda);

        EntityManager em = null;
        EntityTransaction et = null;
        try {
            em = emf.createEntityManager();
            et = em.getTransaction();
            et.begin();
            em.persist(fazendeiro);
            et.commit();
        } catch (Exception ex) {
            if (et != null && et.isActive()) {
                Logger.getGlobal().log(Level.SEVERE,
                        "Cancelando transaÃ§Ã£o com erro. Mensagem: {0}", ex.getMessage());
                et.rollback();
                Logger.getGlobal().info("TransaÃ§Ã£o cancelada.");
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }

        return fazendeiro.getId();
    }

    private static Fazendeiro criarFazendeiro() {
        Fazendeiro fazendeiro = new Fazendeiro();
        fazendeiro.setNome("Fulano da Silva");
        fazendeiro.setEmail("fulano@gmail.com");
        fazendeiro.setLogin("fulano");
        fazendeiro.setSenha("teste");
        fazendeiro.setCpf("534.585.764-45");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 1981);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 25);
        fazendeiro.setDataNascimento(c.getTime());
        fazendeiro.setTipo(TipoUsuario.FAZENDEIRO);
        return fazendeiro;
    }

    public static void criarLocalizacao(Fazenda fazenda) {
        Localizacao localizacao = new Localizacao();
        localizacao.setLogradouro("Rua Iolanda Rodrigues Sobral");
        localizacao.setBairro("Iputinga");
        localizacao.setCidade("Recife");
        localizacao.setEstado("Pernambuco");
        localizacao.setCep("50690-220");
        localizacao.setNumero(550);
        fazenda.setLocalizacao(localizacao);
    }

    public static Fazenda criarFazenda() {
        Fazenda fazenda = new Fazenda();
        fazenda.setCnpj("111.888.999-54");
        fazenda.setNome("Fazenda mundo verde");
        fazenda.setQtdEquitares("10000");
        criarLocalizacao(fazenda);
        return fazenda;
    }

    private static void criarFavorito() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        Favorito favorito = new Favorito();
        Cliente cliente1 = new Cliente();
        Cliente cliente2 = new Cliente();

        try {
            et.begin();
            favorito.setNome("Banana");
            favorito.setDescricao("Banana da terra fresquinha e madura");
            favorito.setTipo("Sem agrotoxico");

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

            cliente2.setNome("Carlos");
            cliente2.setEmail("cliente2@gmail.com");
            cliente2.setLogin("cliente2");
            cliente2.setSenha("senha");
            cliente2.setCpf("222.333.444-13");
            Calendar b = Calendar.getInstance();
            b.set(Calendar.YEAR, 1981);
            b.set(Calendar.MONTH, Calendar.FEBRUARY);
            b.set(Calendar.DAY_OF_MONTH, 25);
            cliente2.setDataNascimento(b.getTime());
            cliente2.addContatos("(81) 99954-6465");
            cliente2.addContatos("(81) 97718-5621");
            cliente2.setTipo(TipoUsuario.CLIENTE);

            favorito.adicionar(cliente1);
            favorito.adicionar(cliente2);

            em.persist(favorito);
            em.flush(); //forÃ§a as alteraÃ§Ãµes a irem para o banco
            // favorito.remover(cliente2); 
            et.commit();
        } catch (Exception ex) {
            if (et != null && et.isActive()) {
                Logger.getGlobal().log(Level.SEVERE,
                        "Cancelando transaÃ§Ã£o com erro. Mensagem: {0}", ex.getMessage());
                et.rollback();
                Logger.getGlobal().info("TransaÃ§Ã£o cancelada.");
            }
        } finally {
            em.close();
        }
    }

}
