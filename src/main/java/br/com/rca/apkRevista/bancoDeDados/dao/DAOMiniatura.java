package br.com.rca.apkRevista.bancoDeDados.dao;

import br.com.rca.apkRevista.bancoDeDados.beans.Miniatura;
import br.com.rca.apkRevista.bancoDeDados.dao.abstracts.DAO;

public class DAOMiniatura extends DAO<Miniatura> {
	private static DAOMiniatura instance;
	public  static DAOMiniatura getInstance() {
		if(instance==null)
			instance = new DAOMiniatura();
		return instance;
	}

	@Override
	public String getBeanClassName() {
		return "Miniatura";
	}

}
