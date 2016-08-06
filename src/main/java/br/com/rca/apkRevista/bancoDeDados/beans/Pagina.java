package br.com.rca.apkRevista.bancoDeDados.beans;

import java.awt.Image;
import java.io.Serializable;
import java.util.Base64;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.rca.apkRevista.bancoDeDados.beans.interfaces.IJSON;
import br.com.rca.apkRevista.ferramentas.Converter;

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
	
	private String imagem;

	public Pagina(){
		
	}
	
	public Pagina(String clientUser, String nomeDaRevista, int nPagina, int largura, int altura, int resolucao,Image imagem) {
		this.user          = clientUser;
		this.nomeDaRevista = nomeDaRevista;
		this.nPagina       = nPagina;
		this.largura       = largura;
		this.altura        = altura;
		this.resolucao     = resolucao;
		
		byte[] imagem64    = Converter.imageToByte(imagem); 
		this.imagem        = Base64.getUrlEncoder().encodeToString(imagem64);
	}
	
	public Image getImagem(){
		byte[] imagem01 = Base64.getUrlDecoder().decode(this.imagem); 
		return Converter.byteToImage(imagem01);
	}

	public JSONObject toJSON() {
		try {
			JSONObject retorno = new JSONObject();
			retorno.append("user",          user);
			retorno.append("nomeDaRevista", nomeDaRevista);
			retorno.append("nPagina",       nPagina);
			retorno.append("largura",       largura);
			retorno.append("altura",        altura);
			retorno.append("resolucao",     resolucao);
			return retorno;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}
