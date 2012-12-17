package org.genericspatialdao.service;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;
import org.genericspatialdao.configuration.DaoConfiguration;
import org.genericspatialdao.exception.DaoException;

/**
 * 
 * @author Joao Savio C. Longo - joaosavio@gmail.com
 * 
 */
public final class EntityManagerFactoryService {

	private static final String FAILED_TO_LOAD_PERSISTENCE_UNIT = "Failed to load default persistence unit: ";
	private static final Logger LOG = Logger
			.getLogger(EntityManagerFactoryService.class);
	private static Map<DaoConfiguration, EntityManagerFactory> factories = new HashMap<DaoConfiguration, EntityManagerFactory>();

	private EntityManagerFactoryService() {

	}

	/**
	 * 
	 * @param configuration
	 * @return an entity manager factory for a target persistence unit
	 */
	public static EntityManagerFactory getEntityManagerFactory(
			DaoConfiguration configuration) {
		if (LOG.isInfoEnabled()) {
			LOG.info("Getting entity manager factory using configuration: "
					+ configuration);
		}
		if (factories.containsKey(configuration)) {
			LOG.debug("Entity manager factory cached. Returning it");
			return factories.get(configuration);
		}

		LOG.debug("Creating a new entity manager factory");
		EntityManagerFactory emf;
		try {
			emf = Persistence.createEntityManagerFactory(
					configuration.getPersistenceUnit(),
					configuration.getProperties());
		} catch (Exception e) {
			String message = FAILED_TO_LOAD_PERSISTENCE_UNIT + e.getMessage();
			if (e.getCause() != null) {
				message = message + ". Cause: " + e.getCause().getMessage();
			}
			LOG.error(message);
			throw new DaoException(message, e);
		}

		factories.put(configuration, emf);
		return emf;
	}

	/**
	 * Close entity manager factories
	 */
	public static void closeFactories() {
		LOG.debug("Closing entity manager factories");
		for (Map.Entry<DaoConfiguration, EntityManagerFactory> entry : factories
				.entrySet()) {
			entry.getValue().close();
		}
		factories.clear();
	}
}