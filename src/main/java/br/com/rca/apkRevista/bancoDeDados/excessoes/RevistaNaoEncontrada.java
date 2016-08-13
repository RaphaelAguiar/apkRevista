package br.com.rca.apkRevista.bancoDeDados.excessoes;

import br.com.rca.apkRevista.bancoDeDados.beans.Cliente;

public class RevistaNaoEncontrada extends ErroRevista{

	public RevistaNaoEncontrada(String revistaProcurada, Cliente cliente) {
		super("A revista " + revistaProcurada + " não foi encontrada para o cliente " + cliente.getUser() + "!",cliente);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4612529339511646018L;

}
