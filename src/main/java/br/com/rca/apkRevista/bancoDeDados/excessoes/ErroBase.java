package br.com.rca.apkRevista.bancoDeDados.excessoes;

public class ErroBase extends Exception  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7541032882940349472L;
	
	public ErroBase(String msg){
		super(msg);
	}
}
