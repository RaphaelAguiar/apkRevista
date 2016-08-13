package br.com.rca.apkRevista.bancoDeDados.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

public abstract class Conexao {
	private static EntityManager     em;
	private static EntityTransaction transaction;
	
	static {
		em          = Persistence.createEntityManagerFactory("revistas").createEntityManager();
		transaction = em.getTransaction();
	}
	
	public static Query getQuery(String hql){
		return em.createQuery(hql);
	}
	
	public static boolean inTransaction(){
		return transaction.isActive();
	}
	
	public static void startTransaction(){
		transaction.begin();
	}
	public static void commit(){
		transaction.commit();
	}
	public static void roolback(){
		transaction.rollback();
	}
	public static void persist(Object object){
		boolean controlarTransacao = !transaction.isActive();
		
		if(controlarTransacao)
			transaction.begin();
		em.persist(object);
		if(controlarTransacao)
			transaction.commit();
	}

	public static void Close() {
		em.close();
	}
}

