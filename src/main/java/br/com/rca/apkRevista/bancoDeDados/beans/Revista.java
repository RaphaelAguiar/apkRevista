package br.com.rca.apkRevista.bancoDeDados.beans;

import java.io.File;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.PolymorphismType;

import br.com.rca.apkRevista.bancoDeDados.beans.enums.Status;
import br.com.rca.apkRevista.bancoDeDados.beans.interfaces.Bean;
import br.com.rca.apkRevista.bancoDeDados.beans.interfaces.Persistente;
import br.com.rca.apkRevista.bancoDeDados.dao.DAOPagina;
import br.com.rca.apkRevista.bancoDeDados.excessoes.PaginaNaoEncontrada;

@Entity
@org.hibernate.annotations.Entity(polymorphism = PolymorphismType.EXPLICIT)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Revista implements Persistente,Bean{
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private int id;
	
	@ManyToOne
	private Cliente    cliente;
	private String     nomeDaRevista;
	private int        edicao;
	private String     subTitulo;
	private int        nPaginas;
	
	private int        largura;
	private int        altura;
	private int        resolucao;
	@Enumerated(EnumType.STRING)
	private Status     status = Status.NAO_DEFINIDO;
	@OneToOne
	Miniatura miniatura;
	
	public Revista(){
		super();
	}
	
	public Revista(Cliente cliente, String nome, int edicao, String subTitulo){
		super();
		this.cliente       = cliente;
		this.nomeDaRevista = nome;
		this.edicao        = edicao;
		this.subTitulo     = subTitulo;
		if (!(this instanceof Miniatura)) {			
			this.miniatura     = new Miniatura(cliente,nome,edicao,subTitulo);
		}
	}

	
	public int getId(){
		return id;
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	public String getNome() {
		return nomeDaRevista;
	}
	public int getNPaginas() {
		return nPaginas;
	}
	
	public List<Pagina> getPaginas(String where, String[] paramns) throws PaginaNaoEncontrada{
		try {
			int lengthParamns2 = paramns.length + (where == "" ? 0 : 1);
			lengthParamns2    += lengthParamns2 == 0 ? 1 : 0;
			String[] paramns2  = new String[lengthParamns2];
			paramns2[0]        = getId() + "";
			for (int i = 1; i < paramns2.length ; i++) {
				paramns2[i] = paramns[i-1];
			}

			if(where == "")
				where = "1 = 1";
			
			List<Pagina> retorno = DAOPagina.getInstance().get("revista_id = ? and " + where, paramns2);
			if(retorno.isEmpty()){
				/*TODO Encontrar uma forma de extrair do where o nome da revista*/
				throw new PaginaNaoEncontrada(0, this);
			}else{
				return retorno;
			}
		} catch (Exception e) {
			if (e instanceof PaginaNaoEncontrada) {
				throw (PaginaNaoEncontrada) e;
			}else{
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public List<Pagina> getPaginas() throws PaginaNaoEncontrada{
		String[] paramns = {""};
		return  getPaginas("", paramns);		
	}

	public int getLargura() {
		return largura;
	}

	public int getAltura() {
		return altura;
	}

	public int getResolucao() {
		return resolucao;
	}

	public String getFolder() {
		return getCliente().getFolder() + File.separator + getNome();
	}

	public void setNPaginas(int nPaginas) {
		this.nPaginas = nPaginas;
		if(!(this instanceof Miniatura))
			this.getMiniatura().setNPaginas(nPaginas);
	}
	
	public void setStatus(Status status) {
		/*TODO Log*/
		if(status==Status.AGUARDANDO_SCANNER){
			try {
				List<Pagina> paginas = getPaginas();
				for (Pagina pagina : paginas) {
					DAOPagina.getInstance().delete(pagina);
				}
			} catch (PaginaNaoEncontrada e) {
				//Sem problemas!
			}
		}
		//System.out.println("Altera��o do status da revista " + getCliente().getUser() + "/" + getNome() + " de " + this.status + " para " + status);
		if (!(this instanceof Miniatura)) {			
			this.getMiniatura().setStatus(status);
		}
		this.status = status;
	}

	public Status getStatus() {
		return status;
	}

	public void setLargura(int largura) {
		this.largura = largura;
	}
	
	public void setAltura(int altura){
		this.altura = altura;
	}
	
	public Miniatura getMiniatura(){
		return miniatura;
	}

	public int getEdicao(){
		return edicao;
	}
	
	public String getSubTitulo(){
		return subTitulo;
	}
}
