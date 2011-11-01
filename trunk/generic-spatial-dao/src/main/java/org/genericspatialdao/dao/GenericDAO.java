package org.genericspatialdao.dao;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.genericspatialdao.exception.DAOException;
import org.genericspatialdao.utils.ConstantsUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;

public class GenericDAO<T> implements DAO<T> {

	protected static final Logger LOG = Logger.getLogger(DAO.class);
	protected final Class<T> entityClass;
	protected EntityManager em = DAOHelper.getEntityManager();

	private static final String FAILED_TO_MERGE_OBJECT = "Failed to merge object: ";
	private static final String FAILED_TO_REMOVE_OBJECT = "Failed to remove object: ";
	private static final String FAILED_TO_PERSIST_OBJECT = "Failed to persist object: ";
	private static final String OBJECT_BY_ID = " object by id ";
	private static final String FINDING = "Finding ";
	private static final String EXECUTING_QUERY = "Executing query: ";
	private static final String FAILED_TO_EXECUTE_QUERY = "Failed to execute query: ";
	private static final String ERROR = "Error: ";
	private static final String RESULT = "Result: ";

	public GenericDAO(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public T find(Object id) {
		LOG.info(FINDING + entityClass.getSimpleName() + OBJECT_BY_ID + id);
		DAOHelper.beginTransaction();
		T t = em.find(entityClass, id);
		LOG.debug(RESULT + t);
		return t;
	}

	public T find(Object id, Map<String, Object> properties) {
		LOG.info(FINDING + entityClass.getSimpleName() + OBJECT_BY_ID + id
				+ " and properties: " + properties);
		DAOHelper.beginTransaction();
		T t = em.find(entityClass, id, properties);
		LOG.debug(RESULT + t);
		return t;
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		LOG.info("Finding all " + entityClass.getSimpleName() + " objects");
		Criteria criteria = DAOHelper.getCriteria(entityClass);
		List<T> result = criteria.list();
		LOG.debug(RESULT + result);
		return result;
	}

	public void persist(T t) {
		LOG.info("Persisting object: " + t);
		try {
			DAOHelper.beginTransaction();
			em.persist(t);
			DAOHelper.commit();
		} catch (Exception e) {
			LOG.error(FAILED_TO_PERSIST_OBJECT + t + ConstantsUtils.DOT_SPACE
					+ e.getMessage());
			DAOHelper.rollback();
			throw new DAOException(FAILED_TO_PERSIST_OBJECT + t
					+ ConstantsUtils.DOT_SPACE + e.getMessage());
		}
	}

	public void remove(T t) {
		LOG.info("Removing object: " + t);
		try {
			DAOHelper.beginTransaction();
			em.remove(t);
			DAOHelper.commit();
		} catch (Exception e) {
			LOG.error(FAILED_TO_REMOVE_OBJECT + t + ConstantsUtils.DOT_SPACE
					+ e.getMessage());
			DAOHelper.rollback();
			throw new DAOException(FAILED_TO_REMOVE_OBJECT + t
					+ ConstantsUtils.DOT_SPACE + e.getMessage());
		}

	}

	public void merge(T t) {
		LOG.info("Merging object: " + t);
		try {
			DAOHelper.beginTransaction();
			em.merge(t);
			DAOHelper.commit();
		} catch (Exception e) {
			LOG.error(FAILED_TO_MERGE_OBJECT + t + ConstantsUtils.DOT_SPACE
					+ e.getMessage());
			DAOHelper.rollback();
			throw new DAOException(FAILED_TO_MERGE_OBJECT + t
					+ ConstantsUtils.DOT_SPACE + e.getMessage());
		}

	}

	public void flush() {
		LOG.info("Flushing session");
		em.flush();
	}

	public List<T> findByCriteria(List<Criterion> list) {
		return findByCriteria(list, null, null, null, null);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findByCriteria(List<Criterion> list,
			ProjectionList projectionList, Order order, Integer firstResult,
			Integer maxResults) {
		try {
			Criteria criteria = DAOHelper.getCriteria(entityClass);
			for (int i = 0; i < list.size(); i++) {
				criteria.add(list.get(i));
			}
			if (projectionList != null) {
				criteria.setProjection(projectionList);
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
			LOG.debug(RESULT + result);
			return result;
		} catch (Exception e) {
			LOG.error(ERROR + e.getMessage());
			throw new DAOException(ERROR + e.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	public List executeSQLQuery(String sql) {
		LOG.info(EXECUTING_QUERY + sql);
		try {
			Query q = DAOHelper.getSession().createSQLQuery(sql);
			List result = q.list();
			LOG.debug(RESULT + result);
			return result;
		} catch (Exception e) {
			LOG.error(FAILED_TO_EXECUTE_QUERY + e.getMessage());
			throw new DAOException(FAILED_TO_EXECUTE_QUERY + e.getMessage());
		}
	}

	public int executeSQLUpdate(String sql) {
		LOG.info(EXECUTING_QUERY + sql);
		try {
			Query q = DAOHelper.getSession().createSQLQuery(sql);
			int result = q.executeUpdate();
			LOG.debug(RESULT + result);
			return result;
		} catch (Exception e) {
			LOG.error(FAILED_TO_EXECUTE_QUERY + e.getMessage());
			throw new DAOException(FAILED_TO_EXECUTE_QUERY + e.getMessage());
		}

	}

}
