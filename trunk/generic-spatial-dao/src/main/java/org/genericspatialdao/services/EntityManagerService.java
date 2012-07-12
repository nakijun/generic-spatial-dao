package org.genericspatialdao.services;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.apache.log4j.Logger;
import org.genericspatialdao.exceptions.DAOException;

/**
 * 
 * @author Joao Savio C. Longo - joaosavio@gmail.com
 * 
 */
public class EntityManagerService {

	private static final Logger LOG = Logger
			.getLogger(EntityManagerService.class);

	private static ThreadLocal<EntityManager> session = new ThreadLocal<EntityManager>();

	/**
	 * Get or create an entity manager
	 * 
	 * @return an entity manager for a target persistence unit
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
	 * Get the entity manager in session
	 * 
	 * @return the entity manager in session
	 */
	public static synchronized EntityManager getEntityManagerFromSession() {
		EntityManager em = session.get();
		if (em == null) {
			throw new DAOException("There is no entity manager in session");
		}
		return em;
	}

	/**
	 * Closes entity manager and remove it from session
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
	 * Closes quietly entity manager and session
	 */
	public static synchronized void closeQuietly() {
		try {
			close();
		} catch (DAOException e) {
			LOG.warn("Exception caught in closeQuietly method: "
					+ e.getMessage());
		}
	}

	/**
	 * Begin a transaction if it is not active
	 */
	public static void beginTransaction() {
		EntityManager em = getEntityManagerFromSession();
		EntityTransaction transaction = em.getTransaction();
		if (!transaction.isActive()) {
			LOG.debug("Beginning transaction");
			transaction.begin();
		}
	}

	/**
	 * Commit if transaction is active
	 */
	public static void commit() {
		EntityManager em = getEntityManagerFromSession();
		EntityTransaction transaction = em.getTransaction();
		if (transaction.isActive()) {
			LOG.info("Commiting");
			transaction.commit();
		} else {
			LOG.warn("Commit invoked but transaction is not active");
		}
	}

	/**
	 * Rollback if transaction is active
	 */
	public static void rollback() {
		EntityManager em = getEntityManagerFromSession();
		EntityTransaction transaction = em.getTransaction();
		if (transaction.isActive()) {
			LOG.info("Rollbacking");
			transaction.rollback();
		} else {
			LOG.warn("Rollback invoked but transaction is not active");
		}
	}
}
