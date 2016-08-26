package br.com.rca.apkRevista.bancoDeDados.beans;

import javax.persistence.Entity;

@Entity
public class Miniatura extends Revista {
	public Miniatura(){
		super();
	}
	
	public Miniatura(Cliente cliente, String nomeDaRevista) {
		super(cliente, nomeDaRevista);
	}
	
	@Override
	public Miniatura getMiniatura(){
		throw new RuntimeException("Uma revista em miniatura não contem uma miniatura!");
	}

	@Override
	public String getFolder(){
		return super.getFolder() + "-min";
	}
}
