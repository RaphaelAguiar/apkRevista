package br.com.rca.apkRevista.bancoDeDados.dao;

import java.util.List;

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
	public List<Revista> get(String where, String[] paramns) throws Exception{
		where += " and revista.class = 'Revista' ";
		return super.get(where, paramns);
	}
	
	@Override
	public String getBeanClassName() {
		return "Revista revista";
	}
	
	@Override
	public void persist(Revista revista){
		if(revista.getStatus()==Status.NAO_DEFINIDO)
			throw new RevistaComStatusNaoDefinido(revista);
		else{
			DAOMiniatura.getInstance().persist(revista.getMiniatura());
			super.persist(revista);
		}
	}
	@Override
	public void delete(Revista revista){
		DAOMiniatura.getInstance().delete(revista.getMiniatura());
		super.delete(revista);
	}
}
