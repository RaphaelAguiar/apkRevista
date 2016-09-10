package br.com.rca.apkRevista.webService;

import java.awt.Image;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.rca.apkRevista.bancoDeDados.beans.Cliente;
import br.com.rca.apkRevista.bancoDeDados.beans.Revista;
import br.com.rca.apkRevista.bancoDeDados.beans.enums.Status;
import br.com.rca.apkRevista.bancoDeDados.dao.DAOCliente;
import br.com.rca.apkRevista.bancoDeDados.excessoes.ClienteNaoEncontrado;
import br.com.rca.apkRevista.bancoDeDados.excessoes.PaginaNaoEncontrada;
import br.com.rca.apkRevista.bancoDeDados.excessoes.RevistaNaoDisponivel;
import br.com.rca.apkRevista.bancoDeDados.excessoes.RevistaNaoEncontrada;
import br.com.rca.apkRevista.bancoDeDados.excessoes.SenhaIncorreta;
import br.com.rca.apkRevista.webService.excessoes.ErroDeEnvio;


@Path("/")
public class WebService extends Service{	
	@GET
	@Path("/obterImagem")
	@Produces("image/png")
	@Consumes(MediaType.APPLICATION_JSON)
	public Image obterImagem(@QueryParam("cliente")       String  user,
			                 @QueryParam("nomeDaRevista") String  nomeDaRevista,
			                 @QueryParam("nPagina")       int     nPagina,
			                 @QueryParam("miniatura")     boolean miniatura) throws JSONException, RevistaNaoEncontrada, RevistaNaoDisponivel, PaginaNaoEncontrada, ClienteNaoEncontrado {
		try{				
			Cliente cliente             = obterCliente(user);
			String[] paramNomeDaRevista = {nomeDaRevista};
			String[] paramNPagina       = {nPagina + ""};				
			Revista revista   = cliente.getRevistas("nomeDaRevista = ?", paramNomeDaRevista).get(0);
			if(miniatura)
				revista = revista.getMiniatura();
			return  revista.getPaginas("nPagina = ?", paramNPagina).get(0).getImagem();
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
	
	@GET
	@Path("/obterStatus")
	@Produces(MediaType.APPLICATION_JSON)
	public String obterStatus(@QueryParam("cliente")       String cliente,
			                  @QueryParam("nomeDaRevista") String nomeDaRevista
			                  ) throws JSONException, RevistaNaoEncontrada{
		try {
			JSONObject retorno      = new JSONObject();
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

	@GET
	@Path("obterMetaDados")
	@Produces(MediaType.APPLICATION_JSON)
	public String obterMetadados(@QueryParam("cliente") String user) throws JSONException, ClienteNaoEncontrado, RevistaNaoEncontrada{
		try {
			Cliente cliente        = obterCliente(user);
			String[] params        = {Status.DISPONIVEL + ""};
			List<Revista> revistas = cliente.getRevistas("Status = ?",params);
			JSONArray retorno      = new JSONArray();
			for (Revista revista : revistas) {
				JSONObject result = new JSONObject();
				result.put("nome", revista.getNome());
				result.put("nPaginas", revista.getNPaginas());
				result.put("edicao", revista.getEdicao());
				result.put("subTitulo", revista.getSubTitulo());
				retorno.put(result);
			}
			return retorno.toString();
		} catch (JSONException e) {
			throw e;
		} catch (ClienteNaoEncontrado e) {
			throw e;	
		} catch (RevistaNaoEncontrada e) {
			/*TODO Criar uma excessão especifica para quando não há revistas para este cliente!*/
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@POST
	@Path("/enviarRevista")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response enviarImagem(@FormDataParam("arquivo")       InputStream inputStream,
								 @FormDataParam("cliente")       String user, 
				                 @FormDataParam("senha")         String senha,
				                 @FormDataParam("nomeDaRevista") String nomeDaRevista,
				                 @FormDataParam("edicao")        int    edicao,
				                 @FormDataParam("subTitulo")     String subTitulo)
		throws JSONException, 
   			   ClienteNaoEncontrado,  
			   SenhaIncorreta, 
			   ErroDeEnvio
	{ 
		try {
			Cliente cliente       = obterCliente(user);
			if(cliente.senhaCorreta(senha)){
				String[] paramNomeDaRevista = {nomeDaRevista};
				try{
					Revista revista      = cliente.getRevistas("nomeDaRevista = ?", paramNomeDaRevista).get(0);
					switch(revista.getStatus()){
						case AGUARDANDO_SCANNER:
							iniciarScanner();
							throw new ErroDeEnvio("A revista " + revista.getNome() + " está na fila do scanner, favor aguardar!");
						case EM_PROCESSAMENTO:
						case GERANDO_IMAGENS:
							throw new ErroDeEnvio("A revista " + revista.getNome() + " está em processamento, favor aguardar!");
						default:
							revista.setStatus(Status.AGUARDANDO_SCANNER); //Deleta as paginas automaticamente
							processar(inputStream, revista);				
					}
											

				}catch(RevistaNaoEncontrada e){	
					Revista revista     = new Revista(cliente, nomeDaRevista,edicao,subTitulo);
					processar(inputStream, revista);
				}
				return Response.ok().build();
			}else{
				throw new SenhaIncorreta(cliente);
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
			return null;
		}
	}
	
	
	/*@GET
	@Path("paginaDeInclusaoDeRevistas.html")
	@Produces(MediaType.TEXT_HTML)
	public InputStream paginaDeInclusaoDeRevistas() throws FileNotFoundException{
		File file = new File(Parametros.PASTA_RAIZ + File.separator + "paginaDeInclusaoDeRevistas.html");
		return new FileInputStream(file);
	}*/
}		
