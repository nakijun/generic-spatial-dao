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

	/**
	 * Begin a transaction if it is not active
	 */
	public static void beginTransaction() {
		EntityManager em = getEntityManagerFromSession();
		EntityTransaction transaction = em.getTransaction();
		if (!transaction.isActive()) {
			LOG.info("Beginning transaction");
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
		}
	}

	/**
	 * @return session for Hibernate
	 */
	public static Session getSession() {
		EntityManager em = getEntityManagerFromSession();
		return ((Session) em.getDelegate());
	}
}
