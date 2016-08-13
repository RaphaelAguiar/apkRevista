package br.com.rca.apkRevista.bancoDeDados.beans.abstracts;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Bean {
	@Id
	@GeneratedValue
	private int id;
	
	public int getId(){
		return id;
	}
}
