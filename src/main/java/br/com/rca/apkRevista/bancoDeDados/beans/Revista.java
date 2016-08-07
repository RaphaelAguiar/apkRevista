package br.com.rca.apkRevista.bancoDeDados.beans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Base64;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.ghost4j.document.DocumentException;
import org.ghost4j.document.PDFDocument;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.rca.apkRevista.bancoDeDados.beans.interfaces.IJSON;
import br.com.rca.apkRevista.ferramentas.Converter;

@Entity
public class Revista implements Serializable, IJSON{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7202943200233575262L;
	@Id
	private String user;
	@Id
	private String nome;
	private int nPaginas;
	
	public Revista(){
		
	}
	
	public Revista(String user,String name, File arquivo) {
		try {
			this.user = user;
			this.nome = name.replace(".pdf", "");
			
			PDFDocument documento = new PDFDocument();
			documento.load(arquivo);
			nPaginas = documento.getPageCount();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public int getNPaginas() {
		return nPaginas;
	}

	public JSONObject toJSON() {
		try {
			JSONObject retorno = new JSONObject();
			retorno.append("user",user);
			retorno.append("nome",nome);
			retorno.append("nPaginas",nPaginas);
			retorno.append("arquivo",getArquivoURL());
		return retorno;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getFilePath(){
		return user + File.separator + nome + ".pdf";
	}
	
	public String getArquivoURL() {
		File   arquivo    = new File(getFilePath());
		byte[] arquivo01  =  Converter.fileToByte(arquivo);
		return Base64.getUrlEncoder().encodeToString(arquivo01);
	}
}
