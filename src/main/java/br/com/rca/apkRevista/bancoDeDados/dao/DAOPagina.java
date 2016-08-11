package br.com.rca.apkRevista.bancoDeDados.dao;

import java.util.List;

import javax.persistence.Query;

import br.com.rca.apkRevista.bancoDeDados.beans.Pagina;
import br.com.rca.apkRevista.bancoDeDados.excessoes.ClienteNaoExiste;
import br.com.rca.apkRevista.bancoDeDados.excessoes.PaginaNaoExiste;
import br.com.rca.apkRevista.bancoDeDados.excessoes.PaginaNaoExisteNestaResolucao;
import br.com.rca.apkRevista.bancoDeDados.excessoes.RevistaNaoExiste;

public class DAOPagina extends DAO<Pagina>{
	private static DAOPagina instance;

	@Override
	public Pagina get(String... keys) throws PaginaNaoExiste, ClienteNaoExiste, RevistaNaoExiste {
		String user          = keys[0];
		String nomeDaRevista = keys[1].replaceAll(".pdf", "");
		int    nPagina       = Integer.parseInt(keys[2]);
		int    largura       = Integer.parseInt(keys[3]);
		int    altura        = Integer.parseInt(keys[4]);
		int    resolucao     = Integer.parseInt(keys[5]);
		
		Query qry = Conexao.getQuery("from Pagina where user          = :user          " + 
										           "and nomeDaRevista = :nomeDaRevista " +
										           "and nPagina       = :nPagina       " +
										           "and resolucao     = :resolucao     " +
									               (altura  != 0 ? "and altura =  :altura  " : "") +
												   (largura != 0 ? "and largura = :largura " : "")
		);
		
		qry.setParameter("user"         , user).
	        setParameter("nomeDaRevista", nomeDaRevista).
	        setParameter("nPagina"      , nPagina).
	        setParameter("resolucao"    , resolucao);
		
		if(largura!=0)
			qry.setParameter("largura", largura);
		if(altura!=0)
			qry.setParameter("altura", altura);
		
		@SuppressWarnings("unchecked")
		List<Pagina> result = (List<Pagina>) qry.getResultList();
		
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
			int nPaginas = e.getNUltimaPagina();
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
				                                                            "and nPagina       = :nPagina       " +
				                                                            "order by resolucao desc,largura*altura desc limit 1").
												               setParameter("user"         , clientUser).
												               setParameter("nomeDaRevista", nomeDaRevista).
												               setParameter("nPagina"      , nPagina).
												               getResultList();
		if(result.isEmpty()){
			int ultimaPagina = DAORevista.getInstance().get(clientUser, nomeDaRevista).getNPaginas();
			throw new PaginaNaoExiste(ultimaPagina);
		}
		else
			return result.get(0);	
	}	
}
