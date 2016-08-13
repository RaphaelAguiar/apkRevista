package br.com.rca.apkRevista.bancoDeDados.excessoes;

import br.com.rca.apkRevista.bancoDeDados.beans.Revista;

public class PaginaNaoEncontrada extends ErroPagina {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3464453429572514743L;
	private int     paginaProcurada;
	
	public PaginaNaoEncontrada(int paginaProcurada,Revista revista){
		super("A pagina " + paginaProcurada + " não foi encontrada na revista " + revista.getNome() + "!",revista);
		this.paginaProcurada = paginaProcurada;
	}

	public int getPaginaProcurada() {
		return paginaProcurada;
	}
}
