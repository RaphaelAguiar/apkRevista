package br.com.rca.apkRevista.bancoDeDados.dao;

import br.com.rca.apkRevista.bancoDeDados.beans.Revista;
import br.com.rca.apkRevista.bancoDeDados.beans.enums.Status;
import br.com.rca.apkRevista.bancoDeDados.dao.abstracts.DAO;
import br.com.rca.apkRevista.bancoDeDados.excessoes.runtime.RevistaComStatusNaoDefinido;

public class DAORevista extends DAO<Revista>{
	private static DAORevista instance;
	public static DAORevista getInstance() {
		if(instance==null)
			instance = new DAORevista();
		return instance;
	}
	@Override
	public String getBeanClassName() {
		return "Revista";
	}
	
	@Override
	public void persist(Revista revista){
		if(revista.getStatus()==Status.NAO_DEFINIDO)
			throw new RevistaComStatusNaoDefinido(revista);
		else
			super.persist(revista);
	}
}
