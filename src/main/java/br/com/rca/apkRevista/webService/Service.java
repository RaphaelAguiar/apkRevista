package br.com.rca.apkRevista.webService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import br.com.rca.apkRevista.bancoDeDados.beans.Cliente;
import br.com.rca.apkRevista.bancoDeDados.beans.Revista;
import br.com.rca.apkRevista.bancoDeDados.beans.enums.Status;
import br.com.rca.apkRevista.bancoDeDados.dao.DAOCliente;
import br.com.rca.apkRevista.bancoDeDados.dao.DAORevista;
import br.com.rca.apkRevista.bancoDeDados.excessoes.ClienteNaoEncontrado;

public class Service {
	public Service(){
		iniciarScanner();
	}
	
	protected void salvarArquivo(InputStream inputStream, String caminho){
	    try {
	    	OutputStream outputStream = new FileOutputStream(new File(caminho));
	        int read = 0;
	        byte[] bytes = new byte[1024];
	        while ((read = inputStream.read(bytes)) != -1) {
	            outputStream.write(bytes, 0, read);
	        }
	        outputStream.flush();
	        outputStream.close();
	    }
	    catch (IOException ioe) {
	        ioe.printStackTrace();
	    }
	}
	//private Process scanner;
	protected void iniciarScanner(){
		/*TODO*/ 
		/*if(scanner==null||!scanner.isAlive()){
			try {
				ProcessBuilder pb = new ProcessBuilder("java", "-jar", Parametros.PASTA_RAIZ + File.separator + "Scanner.jar");
				scanner        =  pb.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}*/
	}
		
	protected void processar(InputStream inputStream, Revista revista) {
		salvarArquivo(inputStream, revista.getFolder() + ".pdf");
		revista.setStatus(Status.AGUARDANDO_SCANNER);
		DAORevista.getInstance().persist(revista);
		iniciarScanner();
	}
	
	protected Cliente obterCliente(String user) throws ClienteNaoEncontrado, Exception{
		try{
			String[] paramCliente       = {user};	
			List<Cliente> list          = DAOCliente.getInstance().get("user = ?", paramCliente);
			if(list.isEmpty())
				throw new ClienteNaoEncontrado(user);
			else
				return list.get(0);
		}catch(ClienteNaoEncontrado e){
			throw e;
		}catch(Exception e){
			throw e;
		}
	}
}
