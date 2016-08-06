package br.com.rca.apkRevista.bancoDeDados.dao;

abstract class DAO<Bean> {	
	protected DAO(){
		
	}
	
	public void persist(Object object) {
		Conexao.persist(object);
	}
	
	public abstract Bean get(String... keys) throws Exception;
}
