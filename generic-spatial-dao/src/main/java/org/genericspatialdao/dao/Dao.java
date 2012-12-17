package org.genericspatialdao.dao;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.genericspatialdao.configuration.CriteriaOptions;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projection;

/**
 * 
 * @author Joao Savio C. Longo - joaosavio@gmail.com
 * 
 */
public interface Dao<T> {

	/**
	 * Begin transaction if there is no one
	 */
	void beginTransaction();

	/**
	 * Commit if there is a transaction
	 */
	void commit();

	/**
	 * Rollback if transaction is active
	 */
	void rollback();

	/**
	 * Close DB connections
	 */
	void close();

	/**
	 * 
	 * @return a Hibernate session
	 */
	Session getSession();

	/**
	 * 
	 * @return a entity manager
	 */
	EntityManager getEntityManager();

	/**
	 * Persist an entity
	 * 
	 * @param t
	 */
	void persist(T... t);

	/**
	 * Persist a list of entities
	 * 
	 * @param list
	 */
	void persist(List<T> list);

	/**
	 * Remove an entity
	 * 
	 * @param t
	 */
	void remove(T... t);

	/**
	 * Remove a list of entities
	 * 
	 * @param list
	 */
	void remove(List<T> list);

	/**
	 * Merge an entity
	 * 
	 * @param t
	 */
	void merge(T... t);

	/**
	 * Merge a list of entities
	 * 
	 * @param list
	 */
	void merge(List<T> list);

	/**
	 * Refresh an entity
	 * 
	 * @param t
	 */
	void refresh(T... t);

	/**
	 * Refresh a list of entities
	 * 
	 * @param list
	 */
	void refresh(List<T> list);

	/**
	 * Clear entity manager
	 */
	void clear();

	/**
	 * Flush session
	 */
	void flush();

	T find(Object id);

	List<T> find(Object... id);

	T find(Object id, Map<String, Object> properties);

	List<T> findAll();

	List<T> findAll(CriteriaOptions criteriaOptions);

	List<T> findByCriteria(List<Criterion> list);

	List<T> findByCriteria(List<Criterion> list, CriteriaOptions criteriaOptions);

	List<?> findByCriteria(List<Criterion> list, Projection projection);

	List<?> findByCriteria(List<Criterion> list, Projection projection,
			CriteriaOptions criteriaOptions);

	T findUniqueByCriteria(List<Criterion> list);

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