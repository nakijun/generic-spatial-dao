package org.genericspatialdao.service;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.apache.log4j.Logger;
import org.genericspatialdao.configuration.DAOConfiguration;
import org.genericspatialdao.exception.DAOException;

/**
 * 
 * @author Joao Savio C. Longo - joaosavio@gmail.com
 * 
 */
public final class EntityManagerService {

	private static final String THERE_IS_NO_SESSION = "There is no session";

	private static final Logger LOG = Logger
			.getLogger(EntityManagerService.class);

	// there is a session for each persistence unit
	private static Map<DAOConfiguration, ThreadLocal<EntityManager>> sessions = new HashMap<DAOConfiguration, ThreadLocal<EntityManager>>();

	private EntityManagerService() {

	}

	/**
	 * 
	 * @param persistenceUnit
	 * @param properties
	 * @return an entity manager using a DAOConfiguration object
	 */
	public static synchronized EntityManager getEntityManager(
			DAOConfiguration configuration) {
		ThreadLocal<EntityManager> session = sessions.get(configuration);
		if (session == null) {
			session = new ThreadLocal<EntityManager>();
			sessions.put(configuration, session);
		}
		EntityManager em = session.get();
		if (em == null) {
			EntityManagerFactory emf = EntityManagerFactoryService
					.getEntityManagerFactory(configuration);
			LOG.debug("Creating entity manager");
			em = emf.createEntityManager(configuration.getProperties());
			session.set(em);
		}
		return em;
	}

	/**
	 * Closes entity manager and remove it from session of a target persistence
	 * unit
	 */
	public static synchronized void close(DAOConfiguration configuration) {
		ThreadLocal<EntityManager> session = sessions.get(configuration);
		checkSession(session);
		EntityManager entityManager = session.get();
		if (entityManager != null) {
			if (entityManager.isOpen()) {
				LOG.info("Closing entity manager");
				entityManager.close();
			}
			LOG.debug("Removing entity manager from session");
			session.set(null);
		}
	}

	public static void closeAll() {
		LOG.info("Closing all entity managers");
		for (DAOConfiguration configuration : sessions.keySet()) {
			close(configuration);
		}
	}

	/**
	 * Closes quietly entity manager and session of a target persistence unit
	 */
	public static synchronized void closeQuietly(DAOConfiguration configuration) {
		try {
			close(configuration);
		} catch (DAOException e) {
			LOG.warn("Exception caught in closeQuietly method: "
					+ e.getMessage());
		}
	}

	/**
	 * Begin a transaction if it is not active
	 */
	public static void beginTransaction(DAOConfiguration configuration) {
		EntityManager em = getEntityManager(configuration);
		EntityTransaction transaction = em.getTransaction();
		if (!transaction.isActive()) {
			LOG.debug("Beginning transaction");
			transaction.begin();
		}
	}

	/**
	 * Commit if transaction is active
	 */
	public static void commit(DAOConfiguration configuration) {
		EntityManager em = getEntityManager(configuration);
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
	public static void rollback(DAOConfiguration configuration) {
		EntityManager em = getEntityManager(configuration);
		EntityTransaction transaction = em.getTransaction();
		if (transaction.isActive()) {
			LOG.info("Rollbacking");
			transaction.rollback();
		} else {
			LOG.warn("Rollback invoked but transaction is not active");
		}
	}

	private static void checkSession(ThreadLocal<EntityManager> session) {
		if (session == null) {
			LOG.error(THERE_IS_NO_SESSION);
			throw new DAOException(THERE_IS_NO_SESSION);
		}
	}
}