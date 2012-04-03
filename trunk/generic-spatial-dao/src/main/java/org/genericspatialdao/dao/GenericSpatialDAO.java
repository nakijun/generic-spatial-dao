package org.genericspatialdao.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.genericspatialdao.exception.DAOException;
import org.genericspatialdao.services.EntityManagerService;
import org.genericspatialdao.utils.ConstantsUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

public class GenericSpatialDAO<T> implements DAO<T> {

	protected static final String FINDING_UNIQUE_BY_CRITERIA = "Finding unique by criteria";
	protected static final String FINDING_BY_CRITERIA = "Finding by criteria";
	protected static final String CAUSE = ". Cause: ";
	protected static final String PERSISTING_OBJECT = "Persisting object: ";
	protected static final String REMOVING_OBJECT = "Removing object: ";
	protected static final String MERGING_OBJECT = "Merging object: ";
	protected static final String REFRESHING_OBJECT = "Refreshing object: ";
	protected static final String FAILED_TO_MERGE = "Failed to merge: ";
	protected static final String FAILED_TO_REMOVE = "Failed to remove: ";
	protected static final String FAILED_TO_PERSIST = "Failed to persist: ";
	protected static final String FAILED_TO_REFRESH = "Failed to refresh: ";
	protected static final String OBJECT_BY_ID = " object by id ";
	protected static final String OBJECTS_BY_IDS = " objects by ids ";
	protected static final String FINDING = "Finding ";
	protected static final String EXECUTING_QUERY = "Executing query: ";
	protected static final String FAILED_TO_EXECUTE_QUERY = "Failed to execute query: ";
	protected static final String ERROR = "Error: ";
	protected static final String RESULT = "Result: ";
	protected static final String EMPTY_LIST = "Empty list";

	protected static final Logger LOG = Logger
			.getLogger(GenericSpatialDAO.class);

	protected final Class<T> entityClass;
	protected final String persistenceUnitName;
	protected final boolean autoTransaction;

	/**
	 * 
	 * @param entityClass
	 */
	public GenericSpatialDAO(Class<T> entityClass) {
		this(entityClass, ConstantsUtils.DEFAULT_PERSISTENCE_UNIT);
	}

	/**
	 * 
	 * @param entityClass
	 * @param persistenceUnitName
	 */
	public GenericSpatialDAO(Class<T> entityClass, String persistenceUnitName) {
		this(entityClass, persistenceUnitName,
				ConstantsUtils.DEFAULT_AUTO_TRANSACTION);
	}

	/**
	 * 
	 * @param entityClass
	 * @param persistenceUnitName
	 * @param autoTransaction
	 */
	public GenericSpatialDAO(Class<T> entityClass, String persistenceUnitName,
			boolean autoTransaction) {
		this.entityClass = entityClass;
		this.persistenceUnitName = persistenceUnitName;
		this.autoTransaction = autoTransaction;
	}

	@Override
	public T find(Object id) {
		LOG.info(FINDING + entityClass.getName() + OBJECT_BY_ID + id);
		autoBeginTransaction();
		T t = getEntityManager().find(entityClass, id);
		if (LOG.isDebugEnabled()) {
			LOG.debug(RESULT + t);
		}
		return t;
	}

	@Override
	public List<T> find(Object... id) {
		LOG.info(FINDING + entityClass.getName() + OBJECTS_BY_IDS
				+ Arrays.toString(id));
		autoBeginTransaction();
		List<T> resultList = new ArrayList<T>();
		for (Object idEntity : id) {
			T result = getEntityManager().find(entityClass, idEntity);
			if (result != null) {
				resultList.add(result);
			}
		}
		if (resultList == null || resultList.isEmpty()) {
			return null;
		}
		return resultList;
	}

	@Override
	public T find(Object id, Map<String, Object> properties) {
		LOG.info(FINDING + entityClass.getSimpleName() + OBJECT_BY_ID + id
				+ " and properties: " + properties);
		autoBeginTransaction();
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
		Criteria criteria = EntityManagerService
				.getSession(persistenceUnitName).createCriteria(entityClass);
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
			autoBeginTransaction();
			for (T entity : list) {
				persist(entity);
			}
			autoCommit();
		} catch (Exception e) {
			LOG.error(FAILED_TO_PERSIST + e.getMessage() + CAUSE + e.getCause());
			autoRollback();
			throw new DAOException(FAILED_TO_PERSIST + e.getMessage() + CAUSE
					+ e.getCause());
		}
	}

	protected void persist(T entity) {
		LOG.info(PERSISTING_OBJECT + entity);
		getEntityManager().persist(entity);
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
			autoBeginTransaction();
			for (T entity : list) {
				remove(entity);
			}
			autoCommit();
		} catch (Exception e) {
			LOG.error(FAILED_TO_REMOVE + e.getMessage() + CAUSE + e.getCause());
			autoRollback();
			throw new DAOException(FAILED_TO_REMOVE + e.getMessage() + CAUSE
					+ e.getCause());
		}
	}

	protected void remove(T entity) {
		LOG.info(REMOVING_OBJECT + entity);
		getEntityManager().remove(entity);
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
			autoBeginTransaction();
			for (T entity : list) {
				merge(entity);
			}
			autoCommit();
		} catch (Exception e) {
			LOG.error(FAILED_TO_MERGE + e.getMessage() + CAUSE + e.getCause());
			autoRollback();
			throw new DAOException(FAILED_TO_MERGE + e.getMessage() + CAUSE
					+ e.getCause());
		}
	}

	protected void merge(T entity) {
		LOG.info(MERGING_OBJECT + entity);
		getEntityManager().merge(entity);
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
			autoBeginTransaction();
			for (T entity : list) {
				refresh(entity);
			}
			autoCommit();
		} catch (Exception e) {
			LOG.error(FAILED_TO_REFRESH + e.getMessage() + CAUSE + e.getCause());
			autoRollback();
			throw new DAOException(FAILED_TO_REFRESH + e.getMessage() + CAUSE
					+ e.getCause());
		}
	}

	protected void refresh(T entity) {
		LOG.info(REFRESHING_OBJECT + entity);
		getEntityManager().refresh(entity);
	}

	@Override
	public void clear() {
		LOG.info("Clearing");
		getEntityManager().clear();
	}

	@Override
	public void flush() {
		LOG.info("Flushing session");
		autoBeginTransaction();
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
			Criteria criteria = getSession().createCriteria(entityClass);
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
			Criteria criteria = getSession().createCriteria(entityClass);
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
			Query q = getSession().createSQLQuery(sql);
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
			Query q = getSession().createSQLQuery(sql);
			int result = q.executeUpdate();
			if (LOG.isDebugEnabled()) {
				LOG.debug(RESULT + result);
			}
			return result;
		} catch (Exception e) {
			LOG.error(FAILED_TO_EXECUTE_QUERY + e.getMessage());
			autoRollback();
			throw new DAOException(FAILED_TO_EXECUTE_QUERY + e.getMessage()
					+ CAUSE + e.getCause());
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List executeHQL(String hql) {
		LOG.info(EXECUTING_QUERY + hql);
		try {
			Query q = getSession().createQuery(hql);
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
			Query q = getSession().createQuery(hqlTemplate);
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
			Query q = getSession().createQuery(hql);
			int result = q.executeUpdate();
			if (LOG.isDebugEnabled()) {
				LOG.debug(RESULT + result);
			}
			return result;
		} catch (Exception e) {
			LOG.error(FAILED_TO_EXECUTE_QUERY + e.getMessage());
			autoRollback();
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
			Criteria criteria = getSession().createCriteria(entityClass);
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
		return EntityManagerService.getEntityManager(persistenceUnitName);
	}

	@Override
	public void beginTransaction() {
		EntityManagerService.beginTransaction(persistenceUnitName);
	}

	@Override
	public void commit() {
		EntityManagerService.commit(persistenceUnitName);
	}

	@Override
	public void rollback() {
		EntityManagerService.rollback(persistenceUnitName);
	}

	@Override
	public void close() {
		EntityManagerService.close();
	}

	@Override
	public String toString() {
		return "DAO of " + entityClass;
	}

	protected Session getSession() {
		return EntityManagerService.getSession(persistenceUnitName);
	}

	protected void autoBeginTransaction() {
		if (autoTransaction) {
			beginTransaction();
		}
	}

	protected void autoRollback() {
		if (autoTransaction) {
			rollback();
		}
	}

	protected void autoCommit() {
		if (autoTransaction) {
			commit();
		}
	}

	protected boolean isEmpty(List<T> list) {
		if (list == null || list.size() == 0) {
			LOG.warn(EMPTY_LIST);
			return true;
		}
		return false;
	}
}
