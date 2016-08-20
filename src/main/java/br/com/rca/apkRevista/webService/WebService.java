package br.com.rca.apkRevista.webService;


import java.awt.Image;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.rca.apkRevista.bancoDeDados.beans.Cliente;
import br.com.rca.apkRevista.bancoDeDados.beans.Pagina;
import br.com.rca.apkRevista.bancoDeDados.beans.Revista;
import br.com.rca.apkRevista.bancoDeDados.beans.enums.Status;
import br.com.rca.apkRevista.bancoDeDados.dao.DAOCliente;
import br.com.rca.apkRevista.bancoDeDados.dao.DAOPagina;
import br.com.rca.apkRevista.bancoDeDados.dao.DAORevista;
import br.com.rca.apkRevista.bancoDeDados.excessoes.ClienteNaoEncontrado;
import br.com.rca.apkRevista.bancoDeDados.excessoes.PaginaNaoEncontrada;
import br.com.rca.apkRevista.bancoDeDados.excessoes.RevistaNaoDisponivel;
import br.com.rca.apkRevista.bancoDeDados.excessoes.RevistaNaoEncontrada;
import br.com.rca.apkRevista.bancoDeDados.excessoes.SenhaIncorreta;
import br.com.rca.apkRevista.webService.excessoes.ErroDeEnvio;

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
	@SuppressWarnings("incomplete-switch")
	@POST
	@Path("/enviarRevista")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public void enviarImagem(@FormDataParam("arquivo") InputStream inputStream,
							 @FormDataParam("request") String request) throws JSONException, 
																			  ClienteNaoEncontrado,  
																			  SenhaIncorreta, 
																			  ErroDeEnvio
	{
		try {
			JSONObject obj        = new JSONObject(request);
			String clienteStr     = obj.getString("cliente");
			String[] clienteParam = {clienteStr};
			List<Cliente> list    = DAOCliente.getInstance().get("user = ?", clienteParam);
			if(list.isEmpty()){
				throw new ClienteNaoEncontrado(clienteStr);
			}else{
				Cliente cliente             = list.get(0);
				String  senha               = obj.getString("senha");
				if(cliente.senhaCorreta(senha)){
					String  nomeDaRevista       = obj.getString("nomeDaRevista");
					String[] paramNomeDaRevista = {nomeDaRevista};
					try{
						Revista revista      = cliente.getRevistas("nomeDaRevista = ?", paramNomeDaRevista).get(0);
						switch(revista.getStatus()){
							case AGUARDANDO_SCANNER:
								iniciarScanner();
								throw new ErroDeEnvio("A revista " + revista.getNome() + " est� na fila do scanner, favor aguardar!");
							case EM_PROCESSAMENTO:
							case GERANDO_IMAGENS:
								throw new ErroDeEnvio("A revista " + revista.getNome() + " est� em processamento, favor aguardar!");
						}
						
						List<Pagina> paginas = revista.getPaginas();
						for (Pagina pagina : paginas) {
							DAOPagina.getInstance().delete(pagina);
						}
						DAORevista.getInstance().delete(revista);
						gerarRevista(inputStream, cliente, nomeDaRevista);
					}catch(RevistaNaoEncontrada e){	
						gerarRevista(inputStream, cliente, nomeDaRevista);
					}
				}else{
					throw new SenhaIncorreta(cliente);
				}
			}
		} catch (JSONException e) {
			throw e;
		} catch (ClienteNaoEncontrado e) {
			throw e;
		} catch (SenhaIncorreta e) {
			throw e;
		} catch (ErroDeEnvio e){
			throw e;
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	private void gerarRevista(InputStream inputStream, Cliente cliente, String nomeDaRevista) {
		Revista revista = new Revista(cliente, nomeDaRevista);
		salvarImagem(inputStream, revista.getFolder() + ".pdf");
		revista.setStatus(Status.AGUARDANDO_SCANNER);
		DAORevista.getInstance().persist(revista);
		iniciarScanner();
	}
}		
