package br.com.rca.apkRevista.bancoDeDados;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Parameter;
import javax.persistence.TemporalType;

import br.com.rca.apkRevista.bancoDeDados.dao.abstracts.Conexao;

public class Query implements javax.persistence.Query {
	private javax.persistence.Query qry;
	
	public Query(javax.persistence.Query qry){
		this.qry = qry;
	}
	
	@SuppressWarnings("rawtypes")
	public List getResultList() {
		List retorno = qry.getResultList();
		for (Object entity : retorno) {
			Conexao.refresh(entity);
		}
		return retorno;
	}

	public Object getSingleResult() {
		return qry.getSingleResult();
	}

	public int executeUpdate() {
		return qry.executeUpdate();
	}

	public Query setMaxResults(int maxResult) {
		qry.setMaxResults(maxResult);
		return this;
	}

	public int getMaxResults() {
		return qry.getMaxResults();
	}

	public Query setFirstResult(int startPosition) {
		qry.setFirstResult(startPosition);
		return this;
	}

	public int getFirstResult() {
		return qry.getFirstResult();
	}

	public Query setHint(String hintName, Object value) {
		qry.setHint(hintName, value);
		return this;
	}

	public Map<String, Object> getHints() {
		return qry.getHints();
	}

	public <T> Query setParameter(Parameter<T> param, T value) {
		qry.setParameter(param, value);
		return this;
	}

	public Query setParameter(Parameter<Calendar> param, Calendar value, TemporalType temporalType) {
		qry.setParameter(param, value, temporalType);
		return this;
	}

	public Query setParameter(Parameter<Date> param, Date value, TemporalType temporalType) {
		qry.setParameter(param, value,temporalType);
		return this;
	}

	public Query setParameter(String name, Object value) {
		qry.setParameter(name, value);
		return this;
	}

	public Query setParameter(String name, Calendar value, TemporalType temporalType) {
		qry.setParameter(name, value, temporalType);
		return this;
	}

	public Query setParameter(String name, Date value, TemporalType temporalType) {
		qry.setParameter(name, value, temporalType);
		return this;
	}

	public Query setParameter(int position, Object value) {
		qry.setParameter(position, value);
		return this;
	}

	public Query setParameter(int position, Calendar value, TemporalType temporalType) {
		qry.setParameter(position, value, temporalType);
		return this;
	}

	public Query setParameter(int position, Date value, TemporalType temporalType) {
		qry.setParameter(position, value, temporalType);
		return this;
	}

	public Set<Parameter<?>> getParameters() {
		return qry.getParameters();
	}

	public Parameter<?> getParameter(String name) {
		return qry.getParameter(name);
	}

	public <T> Parameter<T> getParameter(String name, Class<T> type) {
		return qry.getParameter(name,type);
	}

	public Parameter<?> getParameter(int position) {
		return qry.getParameter(position);
	}

	public <T> Parameter<T> getParameter(int position, Class<T> type) {
		return qry.getParameter(position, type);
	}

	public boolean isBound(Parameter<?> param) {
		return qry.isBound(param);
	}

	public <T> T getParameterValue(Parameter<T> param) {
		return qry.getParameterValue(param);
	}

	public Object getParameterValue(String name) {
		return qry.getParameterValue(name);
	}

	public Object getParameterValue(int position) {
		return qry.getParameterValue(position);
	}

	public Query setFlushMode(FlushModeType flushMode) {
		qry.setFlushMode(flushMode);
		return this;
	}

	public FlushModeType getFlushMode() {
		return qry.getFlushMode();
	}

	public Query setLockMode(LockModeType lockMode) {
		qry.setLockMode(lockMode);
		return this;
	}

	public LockModeType getLockMode() {
		return qry.getLockMode();
	}

	public <T> T unwrap(Class<T> cls) {
		return qry.unwrap(cls);
	}

}
