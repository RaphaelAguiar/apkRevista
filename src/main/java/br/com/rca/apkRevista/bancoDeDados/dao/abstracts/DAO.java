package br.com.rca.apkRevista.bancoDeDados.dao.abstracts;

import java.util.List;

import javax.persistence.Query;

import br.com.rca.apkRevista.bancoDeDados.beans.abstracts.Bean;
import br.com.rca.apkRevista.bancoDeDados.dao.Conexao;

public abstract class DAO<T extends Bean> {
	public void persist(T object) {
		Conexao.persist(object);
	}
	
	@SuppressWarnings("unchecked")
	public List<T>get(String where,String[] paramns) throws Exception{
		if(where == "")
			where = "1 = 1";
		
		Query qry = Conexao.getQuery("from Bean where DTYPE = ? and " + where);
		qry.setParameter(1, getBeanClassName());
		for (int i = 2; i <= paramns.length + 1; i++) {
			String param = paramns[i-2];
			try{
				float asFloat = Float.parseFloat(param);
				int   asInt   = Integer.parseInt(param);
				if(asFloat!=asInt)
					qry.setParameter(i, asFloat);
				else
					qry.setParameter(i, asInt);
			}catch(NumberFormatException e){				
				qry.setParameter(i, param);
			}
		}
		return qry.getResultList();
	}

	public List<T>get() throws Exception{
		return get("",new String[0]);
	}
	
	public abstract String getBeanClassName();
	
	public void delete(T bean){
		boolean controlarTransacao = !Conexao.inTransaction();
		
		if(controlarTransacao)
			Conexao.startTransaction();
		
		Query qry = Conexao.getQuery("delete Bean where id = ?");
		qry.setParameter(1, bean.getId());
		qry.executeUpdate();
		
		if(controlarTransacao)
			Conexao.commit();
	}
}

