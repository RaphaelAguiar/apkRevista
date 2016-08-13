package br.com.rca.apkRevista.webService;

import java.lang.Thread.State;

import br.com.rca.apkRevista.scanner.Scanner;

public class Service {
	private Thread thread;
	public void iniciarScanner(){
		try {
			if(thread==null||thread.getState()==State.TERMINATED){
				thread = new Thread(Scanner.getInstance());
				thread.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Service(){
		iniciarScanner();
	}
	
	public State getThreadScannerState(){
		return thread.getState();
	}
}
