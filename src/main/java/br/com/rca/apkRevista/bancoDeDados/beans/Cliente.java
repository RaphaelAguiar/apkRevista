package br.com.rca.apkRevista.bancoDeDados.beans;

import java.io.File;
import java.util.List;

import javax.persistence.Entity;

import br.com.rca.apkRevista.Parametros;
import br.com.rca.apkRevista.bancoDeDados.beans.abstracts.Bean;
import br.com.rca.apkRevista.bancoDeDados.beans.interfaces.Persistente;
import br.com.rca.apkRevista.bancoDeDados.dao.DAORevista;
import br.com.rca.apkRevista.bancoDeDados.excessoes.RevistaNaoEncontrada;

@Entity	
public class Cliente extends Bean implements Persistente{
	private String user;
	private String password;
	
	public Cliente(){
		super();
	}
	
	public Cliente(String user, String password) {
		this.user     = user;
		this.password = password;
	}

	public String getUser() {
		return user;
	}
	
	public boolean senhaCorreta(String senha){
		return password == senha;
	}
	
	public List<Revista> getRevistas(String where, String[] paramns) throws RevistaNaoEncontrada{
		try {
			/*TODO extrair este trecho, pois é usado tambem em Revista.getPagina() getCliente()*/
			int lengthParamns2 = paramns.length + (where == "" ? 0 : 1);
			String[] paramns2  = new String[lengthParamns2];
			paramns2[0]        = getId() + "";
			for (int i = 1; i < paramns2.length ; i++) {
				paramns2[i] = paramns[i-1];
			}

			if(where == "")
				where = "1 = 1";	
			
			List<Revista> retorno = DAORevista.getInstance().get(" cliente_id = ? and " + where, paramns2);
			if(retorno.isEmpty()){
				/*TODO Encontrar uma forma de extrair do where o nome da revista*/
				throw new RevistaNaoEncontrada("", this);
			}else{
				return retorno;
			}
		} catch (Exception e) {
			if(e instanceof RevistaNaoEncontrada)
				throw (RevistaNaoEncontrada) e;
			else{
				e.printStackTrace();
				return null;
			}
		}
	}

	public String getFolder() {
		return Parametros.PASTA_RAIZ + File.separator +  getUser();
	}
}
