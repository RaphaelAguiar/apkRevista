package br.com.rca.apkRevista.webService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Service {
	public void salvarArquivo(InputStream inputStream, String caminho){
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
	public void iniciarScanner(){
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
	
	public Service(){
		iniciarScanner();
	}
}
