package br.com.rca.apkRevista.bancoDeDados.beans;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import br.com.rca.apkRevista.Parametros;
import br.com.rca.apkRevista.bancoDeDados.beans.interfaces.Bean;
import br.com.rca.apkRevista.bancoDeDados.beans.interfaces.Persistente;
import br.com.rca.apkRevista.bancoDeDados.dao.DAORevista;
import br.com.rca.apkRevista.bancoDeDados.excessoes.RevistaNaoEncontrada;

@Entity
public class Cliente implements Persistente,Bean{
	@Id
	@GeneratedValue
	private int id;
	
	private String user;
	private String password;
	private String email;
	
	//TODO Implementar tratamentos para estas colunas
	//@MapKey(name = "situacao_cliente")
	//@Enumerated(EnumType.STRING)
	//public Situacao situacao = Situacao.ATIVO;
	
	//TODO Nao Implementado
	//Neste caso será necessário criar um controle de permissão por usuario
	//private int nivel = 1;	
	
	//TODO: private Date dataDeCadastro;
	
	public Cliente(){
		super();
	}
	
	public Cliente(String user, String password, String email) {
		this.user     = user;
		this.password = password;
		this.email    = email;
	}

	public int getId(){
		return id;
	}
	
	public String getUser() {
		return user;
	}
	
	private String encryptPassword(String password)
	{
	    String sha1 = "";
	    try
	    {
	        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
	        crypt.reset();
	        crypt.update(password.getBytes("UTF-8"));
	        StringBuilder sb = new StringBuilder();
	        for (byte b : crypt.digest()) {
	            sb.append(String.format("%02x", b));
	        }
	        sha1 = sb.toString();
	    }
	    catch(NoSuchAlgorithmException e)
	    {
	        e.printStackTrace();
	    }
	    catch(UnsupportedEncodingException e)
	    {
	        e.printStackTrace();
	    }
	    return sha1;
	}
	
	
	public boolean senhaCorreta(String senha){
		String sha1 = encryptPassword(senha);
		return password.equals(sha1);
	}
	
	public List<Revista> getRevistas(String where, String[] paramns) throws RevistaNaoEncontrada{
		try {
			/*TODO extrair este trecho, pois é usado tambem em Revista.getPagina() getCliente()*/
			int lengthParamns2 = paramns.length + (where == "" ? 0 : 1);
			lengthParamns2    += lengthParamns2 == 0 ? 1 : 0; 
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
	
	public String getEmail(){
		return email;
	}
}
