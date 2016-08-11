package br.com.rca.apkRevista.scanner;

import java.awt.Image;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import br.com.rca.apkRevista.bancoDeDados.beans.Cliente;
import br.com.rca.apkRevista.bancoDeDados.beans.Pagina;
import br.com.rca.apkRevista.bancoDeDados.beans.Revista;
import br.com.rca.apkRevista.bancoDeDados.dao.Conexao;
import br.com.rca.apkRevista.bancoDeDados.dao.DAOCliente;
import br.com.rca.apkRevista.bancoDeDados.dao.DAOPagina;
import br.com.rca.apkRevista.bancoDeDados.dao.DAORevista;
import br.com.rca.apkRevista.bancoDeDados.excessoes.RevistaNaoExiste;
import br.com.rca.apkRevista.ferramentas.Converter;
import br.com.rca.apkRevista.webService.WebService;


public class Scanner extends Thread{
	public static final String  PASTA_RAIZ         = "C:" + File.separator + "Temp" + File.separator;
	public static final int     RESOLUCAO_PADRAO   = 300;
	public static final String  FORMATO_PADRAO     = "png";
	
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
			boolean teveArquivoEncontrado = false; 
			WebService.addLog("Scanner", "run()", "Procurando novos arquivos pdf's para converter em imagem...");
			for (File pasta : pastasDeClientes) {
				File[] arquivos = pasta.listFiles(new FilenameFilter() {
				    public boolean accept(File dir, String name) {
				        return name.toLowerCase().endsWith(".pdf");
				    }
				});
				for (File arquivo : arquivos) {
					try{
						String user          = pasta.getName();
						String nomeDaRevista = arquivo.getName();
						try{
							DAORevista.getInstance().get(user,nomeDaRevista);
						}catch(RevistaNaoExiste e){
							WebService.addLog("Scanner", "run()", "Um novo arquivo foi encontrado, iniciando processo de converção!");
							teveArquivoEncontrado = true;
							Revista revista = new Revista(user,nomeDaRevista,arquivo);
							List<Image> imagens = Converter.filePdfToImagens(arquivo,user,RESOLUCAO_PADRAO, FORMATO_PADRAO);
							
							Conexao.startTransaction();
							for(int i = 0;i < imagens.size(); i++){
								Image imagem = imagens.get(i);
								Pagina pagina        = new Pagina(user,nomeDaRevista,i+1,imagem.getWidth(null),imagem.getHeight(null),RESOLUCAO_PADRAO);
								DAOPagina.getInstance().persist(pagina);
							}
							DAORevista.getInstance().persist(revista);
							Conexao.commit();							
						}
						
					}catch(Exception e){
						Conexao.roolback();
						e.printStackTrace();
					}
				}			
			}
			if(!teveArquivoEncontrado){
				WebService.addLog("Scanner", "run()", "Todos os arquivos encontrados foram enviados! Encerrando scanner...");
				break;
			}
		}
	}

	public static Scanner getInstance() throws Exception {
		if(instance==null)
			instance = new Scanner();
		return instance;
	}
}
