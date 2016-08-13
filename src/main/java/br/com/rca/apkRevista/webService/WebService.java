package br.com.rca.apkRevista.webService;


import java.awt.Image;
import java.lang.Thread.State;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import br.com.rca.apkRevista.bancoDeDados.dao.DAOCliente;
import br.com.rca.apkRevista.scanner.Scanner;

@Path("/")
public class WebService {
	public WebService(){
		iniciarScanner();
	}
	
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
	public Image transferirImagem(String request) throws Exception{
			JSONObject obj          = new JSONObject(request); 
			String  cliente         = obj.getString("cliente");
			String  nomeDaRevista   = obj.getString("nomeDaRevista");
			int     nPagina         = obj.getInt("nPagina");
			
			String[] clienteParamns = {cliente};
			String[] revistaParamns = {nomeDaRevista};
			String[]    paginaParamns  = {nPagina + ""};
			return DAOCliente.getInstance().
					          get("user = ?",clienteParamns).
					          get(0).
					          getRevistas("nome = ?", revistaParamns).
					          get(0).
					          getPaginas("nPagina = ?", paginaParamns).
					          get(0).
					          getImagem();
	}
}		
