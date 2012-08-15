package org.genericspatialdao.dao.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.genericspatialdao.dao.BasicDAO;
import org.genericspatialdao.dao.DAO;
import org.genericspatialdao.exception.DAOException;
import org.genericspatialdao.service.EntityManagerService;
import org.genericspatialdao.util.ConstantUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

/**
 * 
 * @author Joao Savio C. Longo - joaosavio@gmail.com
 * 
 */
public class GenericSpatialDAO<T> implements DAO<T> {

	protected final Class<T> entityClass;
	protected final String persistenceUnitName;
	protected final boolean autoTransaction;
	protected BasicDAO<T> basicDAO;

	private static final Logger LOG = Logger.getLogger(GenericSpatialDAO.class);

	/**
	 * 
	 * @param entityClass
	 */
	public GenericSpatialDAO(Class<T> entityClass) {
		this(entityClass, ConstantUtils.DEFAULT_PERSISTENCE_UNIT);
	}

	/**
	 * 
	 * @param entityClass
	 * @param persistenceUnitName
	 */
	public GenericSpatialDAO(Class<T> entityClass, String persistenceUnitName) {
		this(entityClass, persistenceUnitName,
				ConstantUtils.DEFAULT_AUTO_TRANSACTION);
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
		basicDAO = getBasicDAO();
	}

	public BasicDAO<T> getBasicDAO() {
		if (basicDAO == null) {
			EntityManager entityManager = EntityManagerService
					.getEntityManager(persistenceUnitName);
			basicDAO = new BasicDAOImpl<T>(entityManager, entityClass);
		}
		return basicDAO;
	}

	@Override
	public T find(Object id) {
		return getBasicDAO().find(id);
	}

	@Override
	public List<T> find(Object... id) {
		return getBasicDAO().find(id);
	}

	@Override
	public T find(Object id, Map<String, Object> properties) {
		return getBasicDAO().find(id, properties);
	}

	@Override
	public List<T> findAll() {
		return findAll(null, null);
	}

	@Override
	public List<T> findAll(Integer firstResult, Integer maxResults) {
		return getBasicDAO().findAll(firstResult, maxResults);
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
			autoRollback();
			throw new DAOException(e);
		}
	}

	protected void persist(T t) {
		getBasicDAO().persist(t);
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
			autoRollback();
			throw new DAOException(e);
		}
	}

	protected void remove(T t) {
		getBasicDAO().remove(t);
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
			autoRollback();
			throw new DAOException(e);
		}
	}

	protected void merge(T t) {
		getBasicDAO().merge(t);
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
			autoRollback();
			throw new DAOException(e);
		}
	}

	protected void refresh(T t) {
		getBasicDAO().refresh(t);
	}

	@Override
	public void clear() {
		getBasicDAO().clear();
	}

	@Override
	public void flush() {
		autoBeginTransaction();
		getBasicDAO().flush();
	}

	@Override
	public List<T> findByCriteria(List<Criterion> list) {
		return findByCriteria(list, null, null, null);
	}

	@Override
	public List<T> findByCriteria(List<Criterion> list, Order order,
			Integer firstResult, Integer maxResults) {
		return getBasicDAO().findByCriteria(list, order, firstResult,
				maxResults);
	}

	@Override
	public T findUniqueByCriteria(List<Criterion> list) {
		return findUniqueByCriteria(list, null, null, null);
	}

	@Override
	public T findUniqueByCriteria(List<Criterion> list, Order order,
			Integer firstResult, Integer maxResults) {
		return getBasicDAO().findUniqueByCriteria(list, order, firstResult,
				maxResults);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List executeSQL(String sql) {
		return getBasicDAO().executeSQL(sql);
	}

	@Override
	public int executeSQLUpdate(String sql) {
		try {
			return getBasicDAO().executeSQLUpdate(sql);
		} catch (Exception e) {
			autoRollback();
			throw new DAOException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List executeHQL(String hql) {
		return getBasicDAO().executeHQL(hql);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List executeHQL(String hqlTemplate, Object... params) {
		return getBasicDAO().executeHQL(hqlTemplate, params);
	}

	@Override
	public int executeHQLUpdate(String hql) {
		try {
			return getBasicDAO().executeHQLUpdate(hql);
		} catch (Exception e) {
			autoRollback();
			throw new DAOException(e);
		}
	}

	@Override
	public void removeAll() {
		try {
			getBasicDAO().removeAll();
		} catch (Exception e) {
			autoRollback();
			throw new DAOException(e);
		}
	}

	@Override
	public long count() {
		return getBasicDAO().count();
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
		EntityManagerService.close(persistenceUnitName);
		basicDAO = null;
	}

	@Override
	public String toString() {
		return "DAO of " + entityClass;
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
			LOG.warn("Empty list");
			return true;
		}
		return false;
	}
}
