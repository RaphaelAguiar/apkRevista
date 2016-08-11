package br.com.rca.apkRevista.webService;


import java.awt.Image;
import java.lang.Thread.State;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.rca.apkRevista.bancoDeDados.beans.Pagina;
import br.com.rca.apkRevista.bancoDeDados.dao.DAOPagina;
import br.com.rca.apkRevista.bancoDeDados.excessoes.ClienteNaoExiste;
import br.com.rca.apkRevista.bancoDeDados.excessoes.PaginaNaoExiste;
import br.com.rca.apkRevista.bancoDeDados.excessoes.PaginaNaoExisteNestaResolucao;
import br.com.rca.apkRevista.bancoDeDados.excessoes.RevistaNaoExiste;
import br.com.rca.apkRevista.scanner.Scanner;

@Path("/")
public class WebService {
	private static int MAX_LOGS          = 100;
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
	}

/*	public WebService(){
		iniciarScanner();
	}*/
	
	private Scanner scanner;
	public void iniciarScanner(){
		if(scanner!=null){
			if(scanner.getState()==State.TERMINATED){
				scanner.start();
			}
		}else{
			try {
				scanner = Scanner.getInstance();
				scanner.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@POST
	@Path("/obterImagem")
	@Produces("image/png")
	@Consumes(MediaType.APPLICATION_JSON)
	public Image transferirImagem(String request){
		try {
			JSONObject obj          = new JSONObject(request); 
			String  clientUser      = obj.getString("clientUser");
			String  nomeDaRevista   = obj.getString("nomeDaRevista");
			int     nPagina         = obj.getInt("nPagina");
			int     largura         = obj.getInt("largura");
			int     altura          = obj.getInt("altura");
			int     resolucao       = obj.getInt("resolucao");
			boolean forcarResolucao = obj.getBoolean("forcarResolucao");
			try{
				Pagina  pagina = DAOPagina.getInstance().get(clientUser,nomeDaRevista,nPagina,largura,altura,resolucao);
				return pagina.getImagem();
			}catch(ClienteNaoExiste e){
				log.add("Cliente " + clientUser + " não encontrado!");
				return null;
			}catch(RevistaNaoExiste e){
				log.add("A Revista " + nomeDaRevista + " do cliente " + clientUser + " não existe!");
				return null;
			}catch(PaginaNaoExiste e){
				log.add("A revista " + nomeDaRevista + " só tem " + e.getNUltimaPagina() + " página(s)!");
				return null;
			}catch(PaginaNaoExisteNestaResolucao e){
				if(forcarResolucao){
					Pagina pagina = new Pagina(clientUser,nomeDaRevista,nPagina,largura,altura,resolucao);
					DAOPagina.getInstance().persist(pagina);
					return null;
				}
			}
			return null;
		} catch (JSONException e) {
			JSONObject retorno = new JSONObject();
			try {
				e.printStackTrace();
				retorno.append("error", e.getClass().getName());
			} catch (JSONException e1) {
				e1.printStackTrace();
				return null;
			}
			return null;
		}
	}
}		
