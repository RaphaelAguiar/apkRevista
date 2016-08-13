package apkRevista;

import org.json.JSONObject;

import br.com.rca.apkRevista.webService.WebService;

public class TesteMetodoTransferirImagem {
	public static void main(String[] args) {
		JSONObject json = new JSONObject(); 
		try {
			json.put("clientUser",      "kuatzak");
			json.put("nomeDaRevista",   "1.nomeDaRevista");
			json.put("nPagina",         "1");
			json.put("largura",         "0");
			json.put("altura",          "0");
			json.put("resolucao",       "300");
			json.put("forcarResolucao", false);
			new WebService().obterImagem(json.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
