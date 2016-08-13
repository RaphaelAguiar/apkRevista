package br.com.rca.apkRevista.bancoDeDados.dao;

import br.com.rca.apkRevista.bancoDeDados.beans.Cliente;
import br.com.rca.apkRevista.bancoDeDados.dao.abstracts.DAO;

public class DAOCliente extends DAO<Cliente>{
	private static DAOCliente instance;
	public  static DAOCliente getInstance() {
		if(instance==null)
			instance = new DAOCliente();
		return instance;
	}
	@Override
	public String getBeanClassName() {
		return "Cliente";
	}
}
