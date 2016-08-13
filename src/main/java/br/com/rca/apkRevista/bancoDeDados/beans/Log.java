package br.com.rca.apkRevista.bancoDeDados.beans;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import br.com.rca.apkRevista.bancoDeDados.beans.abstracts.Bean;

@Entity
public class Log {
	@Id
	private int    id;
	private Date   date;
	private String descricao;
	@ManyToOne
	private Bean   bean;
	
	public Log(){
		
	}

	public Log(String descricao, Bean bean){
		date           = new Date();
		this.descricao = descricao;
	}
	
	public int getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public Bean getBean(){
		return bean;
	}
}
