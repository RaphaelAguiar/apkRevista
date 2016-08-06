package br.com.rca.apkRevista.bancoDeDados.beans;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.rca.apkRevista.bancoDeDados.beans.interfaces.IJSON;

@Entity
public class Cliente implements IJSON{
	
	@Id
	private String user;
	@SuppressWarnings("unused")
	private String password;
	
	public String getUser() {
		return user;
	}

	public JSONObject toJSON() {
		try {
			JSONObject retorno = new JSONObject();
			retorno.append("user", user);
			//o password nao deve sair desta classe!
			return retorno;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}
