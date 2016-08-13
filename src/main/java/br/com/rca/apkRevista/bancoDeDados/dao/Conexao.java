package br.com.rca.apkRevista.bancoDeDados.dao;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class Conexao {
	private static ArrayList<Conexao> conexoes = new ArrayList<Conexao>();
	
	static {
		conexoes.add(new Conexao());
		conexoes.add(new Conexao());
	}
	
	public static Conexao getInstance(int num){
		return conexoes.get(num);
	}
	
	private EntityManager     em;
	private EntityTransaction transaction;
	
	
	public Conexao(){
		em          = Persistence.createEntityManagerFactory("revistas").createEntityManager();
		transaction = em.getTransaction();
	}
	
	public Query getQuery(String hql){
		return em.createQuery(hql);
	}
	
	public boolean inTransaction(){
		return transaction.isActive();
	}
	
	public void startTransaction(){
		transaction.begin();
	}
	public void commit(){
		transaction.commit();
	}
	public void roolback(){
		transaction.rollback();
	}
	public void persist(Object object){
		boolean controlarTransacao = !transaction.isActive();
		
		if(controlarTransacao)
			transaction.begin();
		em.persist(object);
		if(controlarTransacao)
			transaction.commit();
	}

	public void Close() {
		em.close();
	}
}

