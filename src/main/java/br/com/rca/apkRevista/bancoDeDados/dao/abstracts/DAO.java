package br.com.rca.apkRevista.bancoDeDados.dao.abstracts;

import java.util.List;

import javax.persistence.Query;

import br.com.rca.apkRevista.bancoDeDados.beans.interfaces.Bean;


public abstract class DAO<T extends Bean> {
	public void persist(T object) {
		Conexao.persist(object);
	}
	
	@SuppressWarnings("unchecked")
	public List<T>get(String where,String[] paramns) throws Exception{
		if(where == "")
			where = "1 = 1";
		
		Query qry = Conexao.getQuery("from " + getBeanClassName() + " where " + where);
		for (int i = 1; i <= paramns.length; i++) {
			String param = paramns[i-1];
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
		
		Query qry = Conexao.getQuery("delete " + getBeanClassName() + " where id = ?");
		qry.setParameter(1, bean.getId());
		qry.executeUpdate();
		
		if(controlarTransacao)
			Conexao.commit();
	}
}

