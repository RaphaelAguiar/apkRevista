package br.com.rca.apkRevista.webService;


import java.awt.Image;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.rca.apkRevista.bancoDeDados.beans.Cliente;
import br.com.rca.apkRevista.bancoDeDados.beans.Revista;
import br.com.rca.apkRevista.bancoDeDados.dao.DAOCliente;
import br.com.rca.apkRevista.bancoDeDados.excessoes.ClienteNaoEncontrado;
import br.com.rca.apkRevista.bancoDeDados.excessoes.PaginaNaoEncontrada;
import br.com.rca.apkRevista.bancoDeDados.excessoes.RevistaNaoDisponivel;
import br.com.rca.apkRevista.bancoDeDados.excessoes.RevistaNaoEncontrada;

@Path("/")
public class WebService extends Service{	
	@POST
	@Path("/obterImagem")
	@Produces("image/png")
	@Consumes(MediaType.APPLICATION_JSON)
	public Image obterImagem(String request) throws JSONException, RevistaNaoEncontrada, RevistaNaoDisponivel, PaginaNaoEncontrada, ClienteNaoEncontrado {
		try{	
			JSONObject obj = new JSONObject(request);
			String  user                = obj.getString("cliente");
			String  nomeDaRevista       = obj.getString("nomeDaRevista");
			int     nPagina             = obj.getInt("nPagina");
			String[] paramCliente       = {user};
			String[] paramNomeDaRevista = {nomeDaRevista};
			String[] paramNPagina       = {nPagina + ""};
			List<Cliente> list          = DAOCliente.getInstance().get("user = ?", paramCliente);
			if(list.isEmpty())
				throw new ClienteNaoEncontrado(user);
			else{
				Cliente cliente = list.get(0);
				return cliente.getRevistas("nomeDaRevista = ?", paramNomeDaRevista)
						      .get(0)
						      .getPaginas("nPagina = ?", paramNPagina)
						      .get(0)
						      .getImagem();
			}
		} catch (JSONException e) {
			throw e;
		} catch (RevistaNaoEncontrada e) {
			throw e;
		} catch (RevistaNaoDisponivel e) {
			throw e;
		} catch (PaginaNaoEncontrada e) {
			throw e;
		} catch (ClienteNaoEncontrado e){
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@POST
	@Path("obterStatus")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String obterStatus(String request) throws JSONException, RevistaNaoEncontrada{
		try {
			JSONObject retorno      = new JSONObject();
			JSONObject obj          = new JSONObject(request);
			String   cliente        = obj.getString("cliente");
			String   nomeDaRevista  = obj.getString("nomeDaRevista");
			String[] clienteParamns = {cliente};
			String[] revistaParamns = {nomeDaRevista};
			Revista revista         = DAOCliente.getInstance().
									             get("user = ?",clienteParamns).
									             get(0).
									             getRevistas("nomeDaRevista = ?", revistaParamns).
									             get(0);
			retorno.put("Status", revista.getStatus());
			return retorno.toString();
		} catch (JSONException e) {
			throw e;
		} catch (RevistaNaoEncontrada e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}		
