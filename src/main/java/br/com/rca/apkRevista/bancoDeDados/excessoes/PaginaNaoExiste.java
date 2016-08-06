package br.com.rca.apkRevista.bancoDeDados.excessoes;

@SuppressWarnings("serial")
public class PaginaNaoExiste extends Exception {
	private int nDaUltimaPagina;
	
	public PaginaNaoExiste(int nDaUltimaPagina){
		this.nDaUltimaPagina = nDaUltimaPagina;
	}
	
	public int getNUltimaPagina() {
		return nDaUltimaPagina;
	}

}
