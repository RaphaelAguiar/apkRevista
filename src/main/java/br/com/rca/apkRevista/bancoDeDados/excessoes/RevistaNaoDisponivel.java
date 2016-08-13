package br.com.rca.apkRevista.bancoDeDados.excessoes;

import br.com.rca.apkRevista.bancoDeDados.beans.Revista;

public class RevistaNaoDisponivel extends ErroPagina {
	/**
	 * 
	 */
	private static final long serialVersionUID = 118902596159751380L;

	public RevistaNaoDisponivel(Revista revista) {
		super("A revista " + revista.getNome() + " não está disponível!", revista);
	}


}
