package org.genericspatialdao.dao;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.genericspatialdao.exception.DAOException;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

public class GenericSpatialDAO<T> implements DAO<T> {

	protected static final Logger LOG = Logger
			.getLogger(GenericSpatialDAO.class);
	protected final Class<T> entityClass;

	private static final String FINDING_UNIQUE_BY_CRITERIA = "Finding unique by criteria";
	private static final String FINDING_BY_CRITERIA = "Finding by criteria";
	private static final String CAUSE = ". Cause: ";
	private static final String PERSISTING_OBJECT = "Persisting object: ";
	private static final String REMOVING_OBJECT = "Removing object: ";
	private static final String MERGING_OBJECT = "Merging object: ";
	private static final String REFRESHING_OBJECT = "Refreshing object: ";
	private static final String FAILED_TO_MERGE = "Failed to merge: ";
	private static final String FAILED_TO_REMOVE = "Failed to remove: ";
	private static final String FAILED_TO_PERSIST = "Failed to persist: ";
	private static final String FAILED_TO_REFRESH = "Failed to refresh: ";
	private static final String OBJECT_BY_ID = " object by id ";
	private static final String FINDING = "Finding ";
	private static final String EXECUTING_QUERY = "Executing query: ";
	private static final String FAILED_TO_EXECUTE_QUERY = "Failed to execute query: ";
	private static final String ERROR = "Error: ";
	private static final String RESULT = "Result: ";
	private static final String EMPTY_LIST = "Empty list";

	public GenericSpatialDAO(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	@Override
	public T find(Object id) {
		LOG.info(FINDING + entityClass.getName() + OBJECT_BY_ID + id);
		beginTransaction();
		T t = getEntityManager().find(entityClass, id);
		if (LOG.isDebugEnabled()) {
			LOG.debug(RESULT + t);
		}
		return t;
	}

	@Override
	public T find(Object id, Map<String, Object> properties) {
		LOG.info(FINDING + entityClass.getSimpleName() + OBJECT_BY_ID + id
				+ " and properties: " + properties);
		beginTransaction();
		T t = getEntityManager().find(entityClass, id, properties);
		if (LOG.isDebugEnabled()) {
			LOG.debug(RESULT + t);
		}
		return t;
	}

	@Override
	public List<T> findAll() {
		return findAll(null, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll(Integer firstResult, Integer maxResults) {
		LOG.info("Finding all " + entityClass.getSimpleName() + " objects");
		Criteria criteria = PersistenceContext.getSession().createCriteria(
				entityClass);
		if (firstResult != null) {
			criteria.setFirstResult(firstResult);
		}
		if (maxResults != null) {
			criteria.setMaxResults(maxResults);
		}

		List<T> result = (List<T>) criteria.list();
		if (LOG.isDebugEnabled()) {
			LOG.debug(RESULT + result);
		}
		return result;
	}

	@Override
	public void persist(T... t) {
		persist(Arrays.asList(t));
	}

	@Override
	public void persist(List<T> list) {
		if (isEmpty(list)) {
			return;
		}
		try {
			beginTransaction();
			for (T entity : list) {
				LOG.info(PERSISTING_OBJECT + entity);
				getEntityManager().persist(entity);
			}
			commit();
		} catch (Exception e) {
			LOG.error(FAILED_TO_PERSIST + e.getMessage() + CAUSE + e.getCause());
			rollback();
			throw new DAOException(FAILED_TO_PERSIST + e.getMessage() + CAUSE
					+ e.getCause());
		}
	}

	@Override
	public void remove(T... t) {
		remove(Arrays.asList(t));
	}

	@Override
	public void remove(List<T> list) {
		if (isEmpty(list)) {
			return;
		}
		try {
			beginTransaction();
			for (T entity : list) {
				LOG.info(REMOVING_OBJECT + entity);
				getEntityManager().remove(entity);
			}
			commit();
		} catch (Exception e) {
			LOG.error(FAILED_TO_REMOVE + e.getMessage() + CAUSE + e.getCause());
			rollback();
			throw new DAOException(FAILED_TO_REMOVE + e.getMessage() + CAUSE
					+ e.getCause());
		}
	}

	@Override
	public void merge(T... t) {
		merge(Arrays.asList(t));
	}

	@Override
	public void merge(List<T> list) {
		if (isEmpty(list)) {
			return;
		}
		try {
			beginTransaction();
			for (T entity : list) {
				LOG.info(MERGING_OBJECT + entity);
				getEntityManager().merge(entity);
			}
			commit();
		} catch (Exception e) {
			LOG.error(FAILED_TO_MERGE + e.getMessage() + CAUSE + e.getCause());
			rollback();
			throw new DAOException(FAILED_TO_MERGE + e.getMessage() + CAUSE
					+ e.getCause());
		}
	}

	@Override
	public void refresh(T... t) {
		refresh(Arrays.asList(t));
	}

	@Override
	public void refresh(List<T> list) {
		if (isEmpty(list)) {
			return;
		}
		try {
			beginTransaction();
			for (T entity : list) {
				LOG.info(REFRESHING_OBJECT + entity);
				getEntityManager().refresh(entity);
			}
			commit();
		} catch (Exception e) {
			LOG.error(FAILED_TO_REFRESH + e.getMessage() + CAUSE + e.getCause());
			rollback();
			throw new DAOException(FAILED_TO_REFRESH + e.getMessage() + CAUSE
					+ e.getCause());
		}
	}

	@Override
	public void clear() {
		LOG.info("Clearing");
		getEntityManager().clear();
	}

	@Override
	public void flush() {
		LOG.info("Flushing session");
		beginTransaction();
		getEntityManager().flush();
	}

	@Override
	public List<T> findByCriteria(List<Criterion> list) {
		return findByCriteria(list, null, null, null);
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<T> findByCriteria(List<Criterion> list, Order order,
			Integer firstResult, Integer maxResults) {
		LOG.info(FINDING_BY_CRITERIA);
		try {
			Criteria criteria = PersistenceContext.getSession().createCriteria(
					entityClass);
			for (int i = 0; i < list.size(); i++) {
				criteria.add(list.get(i));
			}
			if (order != null) {
				criteria.addOrder(order);
			}
			if (firstResult != null) {
				criteria.setFirstResult(firstResult);
			}
			if (maxResults != null) {
				criteria.setMaxResults(maxResults);
			}

			List result = criteria.list();
			if (LOG.isDebugEnabled()) {
				LOG.debug(RESULT + result);
			}
			return result;
		} catch (Exception e) {
			LOG.error(ERROR + e.getMessage());
			throw new DAOException(ERROR + e.getMessage() + CAUSE
					+ e.getCause());
		}
	}

	@Override
	public T findUniqueByCriteria(List<Criterion> list) {
		return findUniqueByCriteria(list, null, null, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T findUniqueByCriteria(List<Criterion> list, Order order,
			Integer firstResult, Integer maxResults) {
		LOG.info(FINDING_UNIQUE_BY_CRITERIA);
		try {
			Criteria criteria = PersistenceContext.getSession().createCriteria(
					entityClass);
			for (int i = 0; i < list.size(); i++) {
				criteria.add(list.get(i));
			}
			if (order != null) {
				criteria.addOrder(order);
			}
			if (firstResult != null) {
				criteria.setFirstResult(firstResult);
			}
			if (maxResults != null) {
				criteria.setMaxResults(maxResults);
			}

			T result = (T) criteria.uniqueResult();
			if (LOG.isDebugEnabled()) {
				LOG.debug(RESULT + result);
			}
			return result;
		} catch (Exception e) {
			LOG.error(ERROR + e.getMessage());
			throw new DAOException(ERROR + e.getMessage() + CAUSE
					+ e.getCause());
		}
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List executeSQL(String sql) {
		LOG.info(EXECUTING_QUERY + sql);
		try {
			Query q = PersistenceContext.getSession().createSQLQuery(sql);
			List result = q.list();
			if (LOG.isDebugEnabled()) {
				LOG.debug(RESULT + result);
			}
			return result;
		} catch (Exception e) {
			LOG.error(FAILED_TO_EXECUTE_QUERY + e.getMessage());
			throw new DAOException(FAILED_TO_EXECUTE_QUERY + e.getMessage()
					+ CAUSE + e.getCause());
		}
	}

	@Override
	public int executeSQLUpdate(String sql) {
		LOG.info(EXECUTING_QUERY + sql);
		try {
			Query q = PersistenceContext.getSession().createSQLQuery(sql);
			int result = q.executeUpdate();
			if (LOG.isDebugEnabled()) {
				LOG.debug(RESULT + result);
			}
			return result;
		} catch (Exception e) {
			LOG.error(FAILED_TO_EXECUTE_QUERY + e.getMessage());
			throw new DAOException(FAILED_TO_EXECUTE_QUERY + e.getMessage()
					+ CAUSE + e.getCause());
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List executeHQL(String hql) {
		LOG.info(EXECUTING_QUERY + hql);
		try {
			Query q = PersistenceContext.getSession().createQuery(hql);
			List result = q.list();
			if (LOG.isDebugEnabled()) {
				LOG.debug(RESULT + result);
			}
			return result;
		} catch (Exception e) {
			LOG.error(FAILED_TO_EXECUTE_QUERY + e.getMessage());
			throw new DAOException(FAILED_TO_EXECUTE_QUERY + e.getMessage()
					+ CAUSE + e.getCause());
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List executeHQL(String hqlTemplate, Object... params) {
		try {
			Query q = PersistenceContext.getSession().createQuery(hqlTemplate);
			int index = 0;
			for (Object param : params) {
				q.setParameter(index++, param);
			}
			LOG.info(EXECUTING_QUERY + q.getQueryString());
			List result = q.list();
			if (LOG.isDebugEnabled()) {
				LOG.debug(RESULT + result);
			}
			return result;
		} catch (Exception e) {
			LOG.error(FAILED_TO_EXECUTE_QUERY + e.getMessage());
			throw new DAOException(FAILED_TO_EXECUTE_QUERY + e.getMessage()
					+ CAUSE + e.getCause());
		}
	}

	@Override
	public int executeHQLUpdate(String hql) {
		LOG.info(EXECUTING_QUERY + hql);
		try {
			Query q = PersistenceContext.getSession().createQuery(hql);
			int result = q.executeUpdate();
			if (LOG.isDebugEnabled()) {
				LOG.debug(RESULT + result);
			}
			return result;
		} catch (Exception e) {
			LOG.error(FAILED_TO_EXECUTE_QUERY + e.getMessage());
			throw new DAOException(FAILED_TO_EXECUTE_QUERY + e.getMessage()
					+ CAUSE + e.getCause());
		}
	}

	@Override
	public void removeAll() {
		LOG.info("Removing all of type " + entityClass.getSimpleName());
		String hql = "DELETE FROM " + entityClass.getSimpleName();
		executeHQLUpdate(hql);
	}

	@Override
	public long count() {
		LOG.info("Counting rows of " + entityClass.getSimpleName());
		try {
			Criteria criteria = PersistenceContext.getSession().createCriteria(
					entityClass);
			criteria.setProjection(Projections.rowCount());
			long result = (Long) criteria.uniqueResult();
			if (LOG.isDebugEnabled()) {
				LOG.debug(RESULT + result);
			}
			return result;
		} catch (Exception e) {
			LOG.error(ERROR + e.getMessage() + CAUSE + e.getCause());
			throw new DAOException(ERROR + e.getMessage() + CAUSE
					+ e.getCause());
		}
	}

	@Override
	public EntityManager getEntityManager() {
		return PersistenceContext.getEntityManager();
	}

	@Override
	public void beginTransaction() {
		PersistenceContext.beginTransaction();
	}

	@Override
	public void commit() {
		PersistenceContext.commit();
	}

	@Override
	public void rollback() {
		PersistenceContext.rollback();
	}

	@Override
	public void close() {
		PersistenceContext.close();
	}

	@Override
	public String toString() {
		return "DAO of " + entityClass;
	}

	private boolean isEmpty(List<T> list) {
		if (list == null || list.size() == 0) {
			LOG.warn(EMPTY_LIST);
			return true;
		}
		return false;
	}
}
