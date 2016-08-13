package br.com.rca.apkRevista.bancoDeDados.beans.enums;

public enum Status {
	DISPONIVEL,
	GERANDO_IMAGENS,
	EM_PROCESSAMENTO, 
	AGUARDANDO_SCANNER, 

	//Excessoes:
	PDF_NAO_ENCONTRADO,
	ERRO_IO,
	ERRO_NO_PDF,
	ERRO_NO_RENDERER, 
	NAO_DEFINIDO;
}
