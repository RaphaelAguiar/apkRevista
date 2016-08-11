package br.com.rca.apkRevista.bancoDeDados.excessoes;

public class PaginaNaoExiste extends ErroBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3464453429572514743L;
	private int nDaUltimaPagina;
	
	public PaginaNaoExiste(int nDaUltimaPagina){
		this.nDaUltimaPagina = nDaUltimaPagina;
	}
	
	public int getNUltimaPagina() {
		return nDaUltimaPagina;
	}

}
