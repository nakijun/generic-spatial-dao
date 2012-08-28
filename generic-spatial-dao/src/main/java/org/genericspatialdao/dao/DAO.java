package org.genericspatialdao.dao;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

/**
 * 
 * @author Joao Savio C. Longo - joaosavio@gmail.com
 * 
 */
public interface DAO<T> {

	void beginTransaction();

	void commit();

	void rollback();

	void close();

	EntityManager getEntityManager();

	void persist(T... t);

	void persist(List<T> list);

	void remove(T... t);

	void remove(List<T> list);

	void merge(T... t);

	void merge(List<T> list);

	void refresh(T... t);

	void refresh(List<T> list);

	void clear();

	void flush();

	T find(Object id);

	List<T> find(Object... id);

	T find(Object id, Map<String, Object> properties);

	List<T> findAll();

	List<T> findAll(Integer firstResult, Integer maxResults);

	List<T> findByCriteria(List<Criterion> list);

	List<T> findByCriteria(List<Criterion> list, Order order);

	List<T> findByCriteria(List<Criterion> list, Order order,
			Integer firstResult, Integer maxResults);

	T findUniqueByCriteria(List<Criterion> list);

	T findUniqueByCriteria(List<Criterion> list, Order order);

	T findUniqueByCriteria(List<Criterion> list, Order order,
			Integer firstResult, Integer maxResults);

	@SuppressWarnings("rawtypes")
	List executeHQL(String hql);

	@SuppressWarnings("rawtypes")
	List executeHQL(String hqlTemplate, Object... params);

	int executeHQLUpdate(String hql);

	@SuppressWarnings("rawtypes")
	List executeSQL(String sql);

	int executeSQLUpdate(String sql);

	void removeAll();

	long count();
}