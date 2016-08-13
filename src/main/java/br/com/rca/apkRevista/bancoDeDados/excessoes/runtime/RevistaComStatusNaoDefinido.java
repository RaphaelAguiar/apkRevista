package br.com.rca.apkRevista.bancoDeDados.excessoes.runtime;

import br.com.rca.apkRevista.bancoDeDados.beans.Revista;

@SuppressWarnings("serial")
public class RevistaComStatusNaoDefinido extends RuntimeException {

	private Revista revista;
	
	public RevistaComStatusNaoDefinido(Revista revista) {
		super("A revista" + revista.getNome() + " precisa ter um status definido antes de ser gravada no banco de dados!");
		this.revista = revista;
	}

	public Revista getRevista(){
		return revista;
	}
}
