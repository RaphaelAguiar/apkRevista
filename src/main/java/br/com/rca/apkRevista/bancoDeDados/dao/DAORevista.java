package br.com.rca.apkRevista.bancoDeDados.dao;

import java.util.List;

import br.com.rca.apkRevista.bancoDeDados.beans.Revista;
import br.com.rca.apkRevista.bancoDeDados.excessoes.ClienteNaoExiste;
import br.com.rca.apkRevista.bancoDeDados.excessoes.RevistaNaoExiste;

public class DAORevista extends DAO<Revista>{
	private static DAORevista instance;
	
	@Override
	public Revista get(String... keys) throws RevistaNaoExiste, ClienteNaoExiste {
		try{
			@SuppressWarnings("unchecked")
			List<Revista> result = (List<Revista>) Conexao.getQuery("from Revista where user = :user and nome = :nome").
													                setParameter("user", keys[0]).
													                setParameter("nome", keys[1].replace(".pdf", "")).
													                getResultList();
			if(result.isEmpty())
				throw new RevistaNaoExiste();
			else
				return result.get(0);	
		}catch(RevistaNaoExiste e){
			DAOCliente.getInstance().get(keys[0]); //Caso o cliente não exista, levantará a excessão de cliente não existe
			throw e;
		}
	}
	
	public static DAORevista getInstance() {
		if(instance==null)
			instance = new DAORevista();
		return instance;
	}
	
	
}
