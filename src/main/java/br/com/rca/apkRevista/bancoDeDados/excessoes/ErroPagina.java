package br.com.rca.apkRevista.bancoDeDados.excessoes;

import br.com.rca.apkRevista.bancoDeDados.beans.Revista;

public class ErroPagina extends ErroBase{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4244194251724310398L;

	private Revista revista;
	
	public ErroPagina(String msg,Revista revista) {
		super(msg);
		this.revista = revista;
	}

	public Revista getRevista() {
		return revista;
	}
}
