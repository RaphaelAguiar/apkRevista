package br.com.rca.apkRevista.bancoDeDados.excessoes;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.rca.apkRevista.bancoDeDados.beans.interfaces.IJSON;

public class ErroBase extends Exception implements IJSON {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7541032882940349472L;

	public JSONObject toJSON() {
		try {
			JSONObject retorno = new JSONObject();
			retorno.append("error", getClass().getName());
			return retorno;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

}
