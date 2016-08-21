package br.com.rca.apkRevista.bancoDeDados.excessoes;

import br.com.rca.apkRevista.bancoDeDados.beans.Cliente;

public class SenhaIncorreta extends Exception {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7151896833448322894L;
	private Cliente cliente;
	
	public SenhaIncorreta(Cliente cliente) {
		super("A senha informada para o cliente " + cliente.getUser() + " está incorreta!");
	}

	public Cliente getCliente(){
		return cliente;
	}
}
