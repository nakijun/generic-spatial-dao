package org.genericspatialdao.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.genericspatialdao.exception.DAOException;
import org.genericspatialdao.utils.PropertiesUtils;
import org.hibernate.Session;

public class PersistenceContext {

	public static final String DEFAULT_PERSISTENCE_UNIT = "default";
	public static final String DEFAULT_PERSISTENCE_UNIT_PROPERTY = "persistence-unit.default";

	private static final String PERSISTENCE_UNIT_LOADED = "Persistence unit loaded: ";
	private static final String LOADING_PERSISTENCE_UNIT = "Loading persistence unit: ";
	private static final String NO_PERSISTENCE_UNIT_LOADED = "No persistence unit loaded";
	private static final String FAILED_TO_LOAD_PERSISTENCE_UNIT = "Failed to load default persistence unit: ";

	private static final Logger LOG = Logger
			.getLogger(PersistenceContext.class);

	private static ThreadLocal<EntityManager> session = new ThreadLocal<EntityManager>();
	private static EntityManagerFactory factory;

	static {
		String defaultPersistenceUnit = PropertiesUtils
				.getString(DEFAULT_PERSISTENCE_UNIT_PROPERTY);
		if (!StringUtils.isBlank(defaultPersistenceUnit)) {
			loadPersistenceUnit(defaultPersistenceUnit);
		} else {
			loadPersistenceUnit(DEFAULT_PERSISTENCE_UNIT);
		}
	}

	/**
	 * 
	 * @param persistenceUnit
	 */
	public static synchronized void loadPersistenceUnit(String persistenceUnit) {
		closeFactory();
		LOG.info(LOADING_PERSISTENCE_UNIT + persistenceUnit);
		try {
			factory = Persistence.createEntityManagerFactory(persistenceUnit);
			LOG.info(PERSISTENCE_UNIT_LOADED + persistenceUnit);
		} catch (Exception e) {
			LOG.error(FAILED_TO_LOAD_PERSISTENCE_UNIT + e.getMessage()
					+ ". Cause: " + e.getCause().getMessage());
			throw new DAOException(FAILED_TO_LOAD_PERSISTENCE_UNIT
					+ e.getMessage() + ". Cause: " + e.getCause().getMessage());
		}
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
			LOG.info("Beginning transaction");
			transaction.begin();
		}
	}

	/**
	 * Commits if transaction is active
	 * 
	 * @param em
	 */
	public static synchronized void commit() {
		EntityManager em = getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		if (transaction.isActive()) {
			LOG.info("Commiting");
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

	/**
	 * Get session for Hibernate
	 * 
	 * @return
	 */
	public static synchronized Session getSession() {
		EntityManager em = getEntityManager();
		return ((Session) em.getDelegate());
	}
}
