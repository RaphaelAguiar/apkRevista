package apkRevista;

import br.com.rca.apkRevista.webService.WebService;

public class TesteMetodoTransferirImagem {
	public static void main(String[] args) {
		new WebService().transferirImagem("kuatzak", 
				                          "InteracaoComVozAndroid-43.pdf",
				                          1, 
				                          1024, 
				                          768,
				                          300,
				                          true);
	}
}
