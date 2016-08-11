package br.com.rca.apkRevista.bancoDeDados.beans;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.rca.apkRevista.bancoDeDados.beans.interfaces.IJSON;
import br.com.rca.apkRevista.scanner.Scanner;

@Entity
public class Pagina implements Serializable, IJSON{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8363698968138797868L;
	@Id
	private String user;
	@Id
	private String nomeDaRevista;
	@Id
	private int nPagina;
	@Id
	private int largura;
	@Id
	private int altura;
	@Id
	private int resolucao;

	public Pagina(){
		
	}
	
	public Pagina(String clientUser, String nomeDaRevista, int nPagina, int largura, int altura, int resolucao) {
		this.user          = clientUser;
		this.nomeDaRevista = nomeDaRevista.replace(".pdf", "");
		this.nPagina       = nPagina;
		this.largura       = largura;
		this.altura        = altura;
		this.resolucao     = resolucao;
				             
	}
	
	public Image getImagem(){  
		try {
			return ImageIO.read(new File(getImagemPath()));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}	
	}

	
	
	public String getImagemPath() {
		return	Scanner.PASTA_RAIZ        + /*File.separator*/
				user	 	              + File.separator +
				nomeDaRevista             + File.separator +
				n0aEsquerda(resolucao,4)  + File.separator +
				n0aEsquerda(nPagina  ,4)  + "-" + largura + "-" + altura +  "." + Scanner.FORMATO_PADRAO;
	}

	private String n0aEsquerda(int valor, int zeros) {
		String retorno = valor + "";
		int nDigitos   = retorno.length();
		for (int i = 0; i < zeros - nDigitos; i++) {
			retorno = "0" + retorno;
		}
		return retorno;
	}

	public JSONObject toJSON() {
		try {
			JSONObject retorno = new JSONObject();
			retorno.put("user",          user);
			retorno.put("nomeDaRevista", nomeDaRevista);
			retorno.put("nPagina",       nPagina);
			retorno.put("largura",       largura);
			retorno.put("altura",        altura);
			retorno.put("resolucao",     resolucao);
			return retorno;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}
