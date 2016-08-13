package apkRevista;

import java.lang.Thread.State;
import java.util.List;

import org.json.JSONObject;

import br.com.rca.apkRevista.bancoDeDados.beans.Cliente;
import br.com.rca.apkRevista.bancoDeDados.beans.Revista;
import br.com.rca.apkRevista.bancoDeDados.beans.enums.Status;
import br.com.rca.apkRevista.bancoDeDados.dao.DAOCliente;
import br.com.rca.apkRevista.bancoDeDados.dao.DAORevista;
import br.com.rca.apkRevista.bancoDeDados.excessoes.RevistaNaoEncontrada;
import br.com.rca.apkRevista.webService.WebService;

public class TesteScannerThread {
	public static void montarCenario(){
		try {
			String[] paramnsCliente     = {"clienteTeste"};
			List<Cliente> list;
			list = DAOCliente.getInstance().get("user = ?",paramnsCliente);
			Cliente clienteTeste;
			if(list.isEmpty()){
				clienteTeste = new Cliente("clienteTeste","123");
				DAOCliente.getInstance().persist(clienteTeste);
			}else{
				clienteTeste = list.get(0);
			}
			Revista revistaTeste;
			try{
				String[] paramnsRevista = {"revistaTeste"};			
				revistaTeste = clienteTeste.getRevistas("nomeDaRevista = ?", paramnsRevista).get(0);
			}catch(RevistaNaoEncontrada e){
				revistaTeste = new Revista(clienteTeste, "revistaTeste");
			}
			revistaTeste.setStatus(Status.AGUARDANDO_SCANNER);			
			DAORevista.getInstance().persist(revistaTeste);				
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try{
			//Teste em THREAD
			JSONObject request    = new JSONObject();
			request.put("cliente", "clienteTeste");
			request.put("nomeDaRevista", "revistaTeste");
			montarCenario();
			WebService webService = new WebService();
			while(webService.getThreadScannerState()!=State.TERMINATED){
				System.out.println(webService.obterStatus(request.toString()));
				//Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
