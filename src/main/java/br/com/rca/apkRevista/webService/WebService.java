package br.com.rca.apkRevista.webService;


import javax.ws.rs.GET;
import javax.ws.rs.Path;

import br.com.rca.apkRevista.scanner.Scanner;

@Path("/")
public class WebService {
	/*private static int MAX_LOGS          = 100;
	private static ArrayList<String> log = new ArrayList<String>(); 
	public static void addLog(String obj, String method, String msg){
		String logMsg = LocalTime.now() + ": " + obj + " | " + method + " | "+ msg;
		System.out.println(logMsg);
		if(log.size()==MAX_LOGS)
			log.remove(0);
		log.add(logMsg);
	}	
	@GET
	@Path("/log")
	@Produces(MediaType.TEXT_PLAIN)
	public String status(){
		String retorno = "";
		for (String string : log) {
			retorno += string + "\n";
		}
		log.clear();
		return retorno;
	}*/

	@GET
	@Path("/iniciarScanner")
	public void IniciarScanner(){
		try {
			Scanner scanner = Scanner.getInstance();
			scanner.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
/*	@GET
	@Path("/obterImagem")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONObject transferirImagem(String clientUser,String nomeDaRevista, int nPagina ,int largura, int altura, int resolucao, boolean forcarResolucao){
		try{
			Pagina  pagina = DAOPagina.getInstance().get(clientUser,nomeDaRevista,nPagina,largura,altura,resolucao);
			return new JSONObject(pagina.toJSON());
		}catch(ClienteNaoExiste e){
			log.add("Cliente " + clientUser + " não encontrado!");
		}catch(RevistaNaoExiste e){
			log.add("A Revista " + nomeDaRevista + " do cliente " + clientUser + " não existe!");
		}catch(PaginaNaoExiste e){
			log.add("A revista " + nomeDaRevista + " só tem " + e.getNUltimaPagina() + " página(s)!");
		}catch(PaginaNaoExisteNestaResolucao e){
			if(forcarResolucao){
				Pagina pagina = new Pagina(clientUser,nomeDaRevista,nPagina,largura,altura,resolucao);
				DAOPagina.getInstance().persist(pagina);
				return new JSONObject(pagina.toJSON());
			}
		}
		return null;
	}*/
}		
