package br.com.rca.apkRevista.bancoDeDados.dao;

import br.com.rca.apkRevista.bancoDeDados.beans.Pagina;
import br.com.rca.apkRevista.bancoDeDados.dao.abstracts.DAO;

public class DAOPagina extends DAO<Pagina>{
	private static DAOPagina instance;
	public static DAOPagina getInstance() {
		if(instance==null)
			instance = new DAOPagina();
		return instance;
	}
	@Override
	public String getBeanClassName() {
		return "Pagina";
	}	
}
