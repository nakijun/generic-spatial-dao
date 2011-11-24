package org.genericspatialdao.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;

public interface DAO<T> {

	void persist(T t);

	void remove(T t);

	void merge(T t);

	void flush();

	T find(Object id);

	T find(Object id, Map<String, Object> properties);

	List<T> findAll();

	List<T> findByCriteria(List<Criterion> list);

	List<T> findByCriteria(List<Criterion> list, ProjectionList projectionList,
			Order order, Integer firstResult, Integer maxResults);

	@SuppressWarnings("rawtypes")
	List executeHQL(String hql);

	@SuppressWarnings("rawtypes")
	List executeHQL(String hqlTemplate, Object... params);
	
	int executeHQLUpdate(String hql);

	@SuppressWarnings("rawtypes")
	List executeSQLQuery(String sql);

	int executeSQLUpdate(String sql);

	void close();

	void removeAll();

}