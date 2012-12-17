package org.genericspatialdao.service;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.apache.log4j.Logger;
import org.genericspatialdao.configuration.DaoConfiguration;
import org.genericspatialdao.exception.DaoException;

/**
 * 
 * @author Joao Savio C. Longo - joaosavio@gmail.com
 * 
 */
public final class EntityManagerService {

	private static final String THERE_IS_NO_SESSION_FOR_CONFIGURATION = "There is no session for configuration: ";

	private static final Logger LOG = Logger
			.getLogger(EntityManagerService.class);

	// there is a session for each persistence unit
	private static Map<DaoConfiguration, ThreadLocal<EntityManager>> sessionMap = new HashMap<DaoConfiguration, ThreadLocal<EntityManager>>();

	private EntityManagerService() {

	}

	/**
	 * 
	 * @param configuration
	 * @return an entity manager using a DAOConfiguration object
	 */
	public static synchronized EntityManager getEntityManager(
			DaoConfiguration configuration) {
		ThreadLocal<EntityManager> session = sessionMap.get(configuration);
		if (session == null) {
			session = new ThreadLocal<EntityManager>();
			sessionMap.put(configuration, session);
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
	 * Close entity manager and remove it from session of a target persistence
	 * unit
	 * 
	 * @param configuration
	 */
	public static synchronized void close(DaoConfiguration configuration) {
		ThreadLocal<EntityManager> session = sessionMap.get(configuration);
		if (session == null) {
			LOG.info(THERE_IS_NO_SESSION_FOR_CONFIGURATION + configuration);
			return;
		}
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

	/**
	 * Close all entity managers from all configurations
	 */
	public static void closeAll() {
		LOG.info("Closing all entity managers");
		for (DaoConfiguration configuration : sessionMap.keySet()) {
			close(configuration);
		}
	}

	/**
	 * Close quietly entity manager and session of a target persistence unit
	 */
	public static synchronized void closeQuietly(DaoConfiguration configuration) {
		try {
			close(configuration);
		} catch (DaoException e) {
			LOG.warn("Exception caught in closeQuietly method: "
					+ e.getMessage());
		}
	}

	/**
	 * Begin a transaction if it is not active
	 */
	public static void beginTransaction(DaoConfiguration configuration) {
		EntityManager em = getEntityManager(configuration);
		EntityTransaction transaction = em.getTransaction();
		if (!transaction.isActive()) {
			LOG.info("Beginning transaction");
			transaction.begin();
		}
	}

	/**
	 * Commit if transaction is active
	 */
	public static void commit(DaoConfiguration configuration) {
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
	public static void rollback(DaoConfiguration configuration) {
		EntityManager em = getEntityManager(configuration);
		EntityTransaction transaction = em.getTransaction();
		if (transaction.isActive()) {
			LOG.info("Rollbacking");
			transaction.rollback();
		} else {
			LOG.warn("Rollback invoked but transaction is not active");
		}
	}
}