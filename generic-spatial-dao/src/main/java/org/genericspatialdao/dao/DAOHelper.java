package org.genericspatialdao.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;
import org.genericspatialdao.exception.DAOException;
import org.hibernate.Criteria;
import org.hibernate.Session;

public class DAOHelper {

	public static final String DEFAULT_PERSISTENCE_UNIT_NAME = "default";

	private static final String LOADING_PERSISTENCE_UNIT = "Loading persistence unit: ";
	private static final String NO_PERSISTENCE_UNIT_LOADED = "No persistence unit loaded";
	private static final String DEFAULT_PERSISTENCE_UNIT_LOADED = "Default persistence unit loaded";
	private static final String FAILED_TO_LOAD_DEFAULT_PERSISTENCE_UNIT_MESSAGE = "Failed to load default persistence unit: ";
	private static final String TRYING_TO_LOAD_DEFAULT_PERSISTENCE_UNIT = "Trying to load default persistence unit: ";

	private static final Logger LOG = Logger.getLogger(DAOHelper.class);

	private static ThreadLocal<EntityManager> session = new ThreadLocal<EntityManager>();
	private static EntityManagerFactory factory;

	static {
		loadDefaultPersistenceUnit();
	}

	public static void loadDefaultPersistenceUnit() {
		try {
			LOG.info(TRYING_TO_LOAD_DEFAULT_PERSISTENCE_UNIT
					+ DAOHelper.DEFAULT_PERSISTENCE_UNIT_NAME);
			DAOHelper
					.loadPersistenceUnit(DAOHelper.DEFAULT_PERSISTENCE_UNIT_NAME);
			LOG.info(DEFAULT_PERSISTENCE_UNIT_LOADED);
		} catch (Exception e) {
			LOG.warn(FAILED_TO_LOAD_DEFAULT_PERSISTENCE_UNIT_MESSAGE
					+ e.getMessage());
		}
	}

	/**
	 * 
	 * @param persistenceUnit
	 */
	public static synchronized void loadPersistenceUnit(String persistenceUnit) {
		closeFactory();
		LOG.info(LOADING_PERSISTENCE_UNIT + persistenceUnit);
		factory = Persistence.createEntityManagerFactory(persistenceUnit);
	}

	/**
	 * Creates or return an entity manager
	 * 
	 * @return
	 */
	public static synchronized EntityManager getEntityManager() {
		EntityManager em = session.get();
		if (em == null) {
			if (factory == null) {
				LOG.error(NO_PERSISTENCE_UNIT_LOADED);
				throw new DAOException(NO_PERSISTENCE_UNIT_LOADED);
			}

			LOG.debug("Creating entity manager");
			em = factory.createEntityManager();
			session.set(em);
		}

		return em;
	}

	/**
	 * Close entity manager
	 */
	public static synchronized void close() {
		EntityManager em = getEntityManager();
		if (em != null) {
			if (em.isOpen()) {
				LOG.debug("Closing entity manager");
				em.close();
			}
			session.set(null);
		}
	}

	public static synchronized boolean isPersistenceUnitLoaded() {
		if (factory == null) {
			return false;
		}
		return true;
	}

	/**
	 * Close entity manager factory
	 */
	public static synchronized void closeFactory() {
		if (factory != null) {
			if (factory.isOpen()) {
				LOG.debug("Closing entity manager factory");
				factory.close();
			}
			factory = null;
		}
	}

	/**
	 * Begins a transaction if it is not active
	 * 
	 * @param em
	 */
	public static synchronized void beginTransaction() {
		EntityManager em = getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		if (!transaction.isActive()) {
			LOG.debug("Beginning transaction");
			transaction.begin();
		}
	}

	/**
	 * Commits if transaction is not active
	 * 
	 * @param em
	 */
	public static synchronized void commit() {
		EntityManager em = getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		if (transaction.isActive()) {
			LOG.debug("Commiting");
			transaction.commit();
		}
	}

	/**
	 * Rollback if transaction is active
	 * 
	 * @param em
	 */
	public static synchronized void rollback() {
		EntityManager em = getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		if (transaction.isActive()) {
			LOG.info("Rollbacking");
			transaction.rollback();
		}
	}

	public static synchronized Session getSession() {
		EntityManager em = getEntityManager();
		return ((Session) em.getDelegate());
	}

	public static synchronized Criteria getCriteria(Class<?> entityClass) {
		return getSession().createCriteria(entityClass);
	}
}
