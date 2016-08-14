package apkRevista;

import org.json.JSONObject;

import br.com.rca.apkRevista.webService.WebService;

public class TesteMetodoTransferirImagem {
	public static void main(String[] args) {
		JSONObject json = new JSONObject(); 
		try {
			json.put("cliente",         "clienteTeste");
			json.put("nomeDaRevista",   "revistaTeste");
			json.put("nPagina",         "1");
			new WebService().obterImagem(json.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
