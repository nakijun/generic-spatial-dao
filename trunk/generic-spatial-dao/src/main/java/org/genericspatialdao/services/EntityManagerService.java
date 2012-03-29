package org.genericspatialdao.services;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.apache.log4j.Logger;
import org.genericspatialdao.exception.DAOException;
import org.hibernate.Session;

/**
 * 
 * @author joaosavio
 * 
 */
public class EntityManagerService {

	private static final Logger LOG = Logger
			.getLogger(EntityManagerService.class);

	private static ThreadLocal<EntityManager> session = new ThreadLocal<EntityManager>();

	/**
	 * Load a persistence unit
	 * 
	 * @param persistenceUnit
	 * 
	 *            public static synchronized void loadPersistenceUnit(String
	 *            persistenceUnit) { closeFactory();
	 *            LOG.info(LOADING_PERSISTENCE_UNIT + persistenceUnit); try {
	 *            factory =
	 *            Persistence.createEntityManagerFactory(persistenceUnit);
	 *            LOG.info(PERSISTENCE_UNIT_LOADED + persistenceUnit); } catch
	 *            (Exception e) { LOG.error(FAILED_TO_LOAD_PERSISTENCE_UNIT +
	 *            e.getMessage() + ". Cause: " + e.getCause().getMessage());
	 *            throw new DAOException(FAILED_TO_LOAD_PERSISTENCE_UNIT +
	 *            e.getMessage() + ". Cause: " + e.getCause().getMessage()); } }
	 */

	/**
	 * @return an entity manager of a target persistence unit
	 */
	public static synchronized EntityManager getEntityManager(
			String persistenceUnitName) {
		EntityManager em = session.get();
		if (em == null) {
			EntityManagerFactory emf = EntityManagerFactoryService
					.getEntityManagerFactory(persistenceUnitName);
			LOG.debug("Creating entity manager");
			em = emf.createEntityManager();
			session.set(em);
		}
		return em;
	}

	/**
	 * Close entity manager remove it from session
	 */
	public static synchronized void close() {
		EntityManager em = session.get();
		if (em != null) {
			if (em.isOpen()) {
				LOG.info("Closing entity manager");
				em.close();
			}
			LOG.debug("Removing entity manager from session");
			session.set(null);
		}
	}

	/**
	 * Close quietly entity manager and session
	 */
	public static synchronized void closeQuietly() {
		try {
			close();
		} catch (DAOException e) {

		}
	}

	/*
	 * public static synchronized boolean isPersistenceUnitLoaded() { if
	 * (factory == null) { return false; } return true; }
	 */

	/**
	 * Close entity manager factory
	 * 
	 * public static synchronized void closeFactory() { closeQuietly(); if
	 * (factory != null) { if (factory.isOpen()) {
	 * LOG.info("Closing entity manager factory"); factory.close(); } factory =
	 * null; } }
	 */

	/**
	 * Begins a transaction if it is not active
	 */
	public static synchronized void beginTransaction(String persistenceUnitName) {
		EntityManager em = getEntityManager(persistenceUnitName);
		EntityTransaction transaction = em.getTransaction();
		if (!transaction.isActive()) {
			LOG.info("Beginning transaction");
			transaction.begin();
		}
	}

	/**
	 * Commits if transaction is active
	 */
	public static synchronized void commit(String persistenceUnitName) {
		EntityManager em = getEntityManager(persistenceUnitName);
		EntityTransaction transaction = em.getTransaction();
		if (transaction.isActive()) {
			LOG.info("Commiting");
			transaction.commit();
		}
	}

	/**
	 * Rollback if transaction is active
	 */
	public static synchronized void rollback(String persistenceUnitName) {
		EntityManager em = getEntityManager(persistenceUnitName);
		EntityTransaction transaction = em.getTransaction();
		if (transaction.isActive()) {
			LOG.info("Rollbacking");
			transaction.rollback();
		}
	}

	/**
	 * @return session for Hibernate
	 */
	public static synchronized Session getSession(String persistenceUnitName) {
		EntityManager em = getEntityManager(persistenceUnitName);
		return ((Session) em.getDelegate());
	}
}
