package br.com.rca.apkRevista.bancoDeDados.excessoes;

public class ClienteNaoEncontrado extends ErroBase{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4301008640434048227L;
	private String cliente;

	public ClienteNaoEncontrado(String cliente) {
		super("O Cliente " + cliente + " não existe!");
		this.cliente = cliente;
	}

	public String getCliente() {
		return cliente;
	}

}
