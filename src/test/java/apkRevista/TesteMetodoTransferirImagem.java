package apkRevista;

import br.com.rca.apkRevista.webService.WebService;

public class TesteMetodoTransferirImagem {
	public static void main(String[] args) {
		new WebService().transferirImagem("kuatzak", 
				                          "INCRICAOMEIJOYCE.pdf",
				                          1, 
				                          2481, 
				                          3507,
				                          300,
				                          true);
	}
}
