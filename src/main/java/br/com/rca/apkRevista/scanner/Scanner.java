package br.com.rca.apkRevista.scanner;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import br.com.rca.apkRevista.bancoDeDados.beans.Cliente;
import br.com.rca.apkRevista.bancoDeDados.beans.Revista;
import br.com.rca.apkRevista.bancoDeDados.dao.DAOCliente;
import br.com.rca.apkRevista.bancoDeDados.dao.DAORevista;
import br.com.rca.apkRevista.webService.WebService;

public class Scanner extends Thread{
	private static final String  PASTA_RAIZ         = "C:" + File.separator + "Temp" + File.separator;
	private static final int     TEMPO_ENTRE_BUSCAS =  30;
	
	private static Scanner instance;
	
	private ArrayList<File>   pastasDeClientes = new ArrayList<File>();
	
	private Scanner() throws Exception{
		File main               = new File(PASTA_RAIZ);
		if(!main.isDirectory()) 
			throw new Exception("O endereço informado para pasta raiz não corresponde a uma pasta!");
		carregarPastaDeClientes();
	}
	
	public void carregarPastaDeClientes(){
		pastasDeClientes.clear();
		List<Cliente> clientes = DAOCliente.getInstance().getTodosClientes();
		for (Cliente cliente : clientes) {
			String caminho        = PASTA_RAIZ + cliente.getUser();
			File   pastaDoCliente = new File(caminho);
			
			if(!pastaDoCliente.exists()){
				if(!pastaDoCliente.mkdir())
					throw new RuntimeException("Erro ao tentar criar a pasta do cliente " + cliente.getUser());
			}
			if(pastaDoCliente.isDirectory()) 
				pastasDeClientes.add(pastaDoCliente);
			else
				throw new RuntimeException("O arquivo " + caminho + " não é uma pasta!");
		}
	}
	
	@Override
	public void run() {
		WebService.addLog("Scanner", "run()", "Scanner em execução!");
		while(true){
			for (File pasta : pastasDeClientes) {
				File[] arquivos = pasta.listFiles(new FilenameFilter() {
				    public boolean accept(File dir, String name) {
				        return name.toLowerCase().endsWith(".pdf");
				    }
				});
				for (File arquivo : arquivos) {
					Revista revista = new Revista(pasta.getName(),arquivo.getName(),arquivo);	
					DAORevista.getInstance().persist(revista);
				}			
			}
			synchronized(this){
				try {
					WebService.addLog("Scanner", "run()", "Varredura Executada!");
					wait(TEMPO_ENTRE_BUSCAS * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				} catch(Exception e){
					e.printStackTrace();
					break;
				}
			}
		}
		WebService.addLog("Scanner", "run()", "Erro de Interrupção no Scanner!");
	}
	
	public static Scanner getInstance() throws Exception {
		if(instance==null)
			instance = new Scanner();
		return instance;
	}
}
