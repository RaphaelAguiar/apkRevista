package br.com.rca.apkRevista.bancoDeDados.excessoes;

import br.com.rca.apkRevista.bancoDeDados.beans.Cliente;

public class ErroRevista extends ErroBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6225765196501808775L;
	private Cliente cliente;

	public ErroRevista(String msg, Cliente cliente) {
		super(msg);
		this.cliente = cliente;
	}

	public Cliente getCliente() {
		return cliente;
	}

}
