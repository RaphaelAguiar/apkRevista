package br.com.rca.apkRevista.bancoDeDados.dao;

import java.util.List;

import br.com.rca.apkRevista.bancoDeDados.beans.Pagina;
import br.com.rca.apkRevista.bancoDeDados.beans.Revista;
import br.com.rca.apkRevista.bancoDeDados.excessoes.ClienteNaoExiste;
import br.com.rca.apkRevista.bancoDeDados.excessoes.PaginaNaoExiste;
import br.com.rca.apkRevista.bancoDeDados.excessoes.PaginaNaoExisteNestaResolucao;
import br.com.rca.apkRevista.bancoDeDados.excessoes.RevistaNaoExiste;

public class DAOPagina extends DAO<Pagina>{
	private static DAOPagina instance;

	@Override
	public Pagina get(String... keys) throws PaginaNaoExiste, ClienteNaoExiste, RevistaNaoExiste {
		@SuppressWarnings("unchecked")
		List<Pagina> result = (List<Pagina>) Conexao.getQuery("from Pagina where user          = :user          " + 
		                                                                    "and nomeDaRevista = :nomeDaRevista " +
				                                                            "and pagina        = :pagina        " +
				                                                            "and largura       = :largura       " +
				                                                            "and altura        = :altura        " +
				                                                            "and resolucao     = :resolucao     ").
												               setParameter("user"         , keys[0]).
												               setParameter("nomeDaRevista", keys[1]).
												               setParameter("pagina"       , keys[2]).
												               setParameter("largura"      , keys[3]).
												               setParameter("altura"       , keys[4]).
												               setParameter("resolucao"    , keys[5]).
												               getResultList();
		if(result.isEmpty()){
			int ultimaPagina = DAORevista.getInstance().get(keys[0], keys[1]).getNPaginas();
			throw new PaginaNaoExiste(ultimaPagina);	
		}
		return result.get(0);	
	}		

	public Pagina get(String clientUser, String nomeDaRevista, int pagina, int largura, int altura, int resolucao) throws ClienteNaoExiste,
	                                                                                                                      RevistaNaoExiste,
	                                                                                                                      PaginaNaoExiste, 
	                                                                                                                      PaginaNaoExisteNestaResolucao{
		try{
			return getInstance().get(clientUser,nomeDaRevista,pagina + "",largura + "", altura + "", resolucao + "");
		}catch(PaginaNaoExiste e){
			Revista revista = DAORevista.getInstance().get(clientUser, nomeDaRevista); //Caso a revista não exista levantará excessão
			int nPaginas = revista.getNPaginas();
			if (pagina > 0 && pagina <= nPaginas)
				throw new PaginaNaoExisteNestaResolucao();
			else
				throw e;
		}
	}

	public static DAOPagina getInstance() {
		if(instance==null)
			instance = new DAOPagina();
		return instance;
	}

	public Pagina getMaiorResolucao(String clientUser, String nomeDaRevista, int nPagina) throws PaginaNaoExiste, ClienteNaoExiste, RevistaNaoExiste {
		@SuppressWarnings("unchecked")
		List<Pagina> result = (List<Pagina>) Conexao.getQuery("from Pagina where user          = :user          " + 
		                                                                    "and nomeDaRevista = :nomeDaRevista " +
				                                                            "and pagina        = :pagina        " +
				                                                            "order by resolucao desc,largura*altura desc limit 1").
												               setParameter("user"         , clientUser).
												               setParameter("nomeDaRevista", nomeDaRevista).
												               setParameter("pagina"       , nPagina).
												               getResultList();
		if(result.isEmpty()){
			int ultimaPagina = DAORevista.getInstance().get(clientUser, nomeDaRevista).getNPaginas();
			throw new PaginaNaoExiste(ultimaPagina);
		}
		else
			return result.get(0);	
	}	
}
