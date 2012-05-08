package org.genericspatialdao.services;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;
import org.genericspatialdao.exception.DAOException;

/**
 * 
 * @author joaosavio
 * 
 */
public class EntityManagerFactoryService {

	private static final String FAILED_TO_LOAD_PERSISTENCE_UNIT = "Failed to load default persistence unit: ";
	private static final Logger LOG = Logger
			.getLogger(EntityManagerFactoryService.class);
	private static Map<String, EntityManagerFactory> factories = new HashMap<String, EntityManagerFactory>();

	/**
	 * 
	 * @param persistenceUnitName
	 * @return an entity manager factory for a target persistence unit
	 */
	public static EntityManagerFactory getEntityManagerFactory(
			String persistenceUnitName) {
		if (factories.containsKey(persistenceUnitName)) {
			return factories.get(persistenceUnitName);
		}
		EntityManagerFactory emf;
		try {
			emf = Persistence.createEntityManagerFactory(persistenceUnitName);
		} catch (Exception e) {
			LOG.error(FAILED_TO_LOAD_PERSISTENCE_UNIT + e.getMessage()
					+ ". Cause: " + e.getCause().getMessage());
			throw new DAOException(FAILED_TO_LOAD_PERSISTENCE_UNIT
					+ e.getMessage() + ". Cause: " + e.getCause().getMessage());
		}
		factories.put(persistenceUnitName, emf);
		return emf;
	}

	/**
	 * Close entity manager factories
	 */
	public static void closeFactories() {
		LOG.info("Closing entity manager factories");
		for (Map.Entry<String, EntityManagerFactory> entry : factories
				.entrySet()) {
			entry.getValue().close();
		}
		factories.clear();
	}
}
