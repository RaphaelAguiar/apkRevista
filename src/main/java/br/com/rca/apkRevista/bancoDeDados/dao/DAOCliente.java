package br.com.rca.apkRevista.bancoDeDados.dao;

import java.util.List;

import br.com.rca.apkRevista.bancoDeDados.beans.Cliente;
import br.com.rca.apkRevista.bancoDeDados.excessoes.ClienteNaoExiste;

public class DAOCliente extends DAO<Cliente>{
	private static DAOCliente instance;
	
	@SuppressWarnings("unchecked")
	public List<Cliente> getTodosClientes() {
		return (List<Cliente>) Conexao.getQuery("from Cliente").getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Cliente get(String... keys) throws ClienteNaoExiste {
		List<Cliente> result = (List<Cliente>) Conexao.getQuery("from Cliente where user = :user").
												                setParameter("user", keys[0]).
												                getResultList();
		if(result.isEmpty())
			throw new ClienteNaoExiste();
		else
			return result.get(0);
	}

	public static DAOCliente getInstance() {
		if(instance==null)
			instance = new DAOCliente();
		return instance;
	}

}
