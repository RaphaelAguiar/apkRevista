package br.com.rca.apkRevista.bancoDeDados.dao;

import java.util.List;

import br.com.rca.apkRevista.bancoDeDados.beans.Revista;
import br.com.rca.apkRevista.bancoDeDados.excessoes.ClienteNaoExiste;
import br.com.rca.apkRevista.bancoDeDados.excessoes.RevistaNaoExiste;

public class DAORevista extends DAO<Revista>{
	private static DAORevista instance;
	
	@Override
	public Revista get(String... keys) throws Exception {
		@SuppressWarnings("unchecked")
		List<Revista> result = (List<Revista>) Conexao.getQuery("from Revista where user = :user and nomeDaRevista = :nomeDaRevista").
												                setParameter("user", keys[0]).
												                setParameter("nomeDaRevista",keys[1]).
												                getResultList();
		if(result.isEmpty())
			throw new RevistaNaoExiste();
		else
			return result.get(0);	
	}
	
	public Revista get(String user, String nomeDaRevista) throws ClienteNaoExiste,RevistaNaoExiste {
		try{
			return getInstance().get(user,nomeDaRevista);
		}catch(RevistaNaoExiste e){
			DAOCliente.getInstance().get(user); //Caso o cliente não exista, levantará a excessão de cliente não existe
			throw e;
		}
	}

	public static DAORevista getInstance() {
		if(instance==null)
			instance = new DAORevista();
		return instance;
	}
	
	
}
