package br.com.rca.apkRevista.bancoDeDados.beans;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import br.com.rca.apkRevista.Parametros;
import br.com.rca.apkRevista.bancoDeDados.beans.enums.Status;
import br.com.rca.apkRevista.bancoDeDados.beans.interfaces.Bean;
import br.com.rca.apkRevista.bancoDeDados.beans.interfaces.Persistente;
import br.com.rca.apkRevista.bancoDeDados.excessoes.RevistaNaoDisponivel;

@Entity
public class Pagina implements Persistente,Bean{
	@Id
	@GeneratedValue
	private int id;
	
	@ManyToOne
	private Revista revista;
	private int     nPagina;

	@Transient
	private Image imagem;
	
	public Pagina(){
		super();
	}
	
	public Pagina(Revista revista, int nPagina){
		super();
		this.revista = revista;
		this.nPagina = nPagina;
	}
	
	public int getId(){
		return id;
	}
	
	public Revista getRevista() {
		return revista;
	}
	public int getNPagina() {
		return nPagina;
	}

	public String getFolder() {
		return revista.getFolder() + '-' + getNPagina() + "." + Parametros.FORMATO_PADRAO;
	}
	
	public Image getImagem() throws RevistaNaoDisponivel{
		if(imagem==null){
			try {
				if(revista.getStatus()!=Status.DISPONIVEL)
					throw new RevistaNaoDisponivel(revista);
				else{
					String caminho = getFolder();
					imagem         = ImageIO.read(new File(caminho));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return imagem;
	}
}
