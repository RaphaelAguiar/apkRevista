package apkRevista;

import br.com.rca.apkRevista.scanner.Scanner;

public class TesteScanner {
	public static void main(String[] args) {
		try {
			Scanner scanner = Scanner.getInstance();
			scanner.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
