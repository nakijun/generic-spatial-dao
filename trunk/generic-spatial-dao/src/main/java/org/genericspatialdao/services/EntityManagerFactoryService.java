package org.genericspatialdao.services;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;
import org.genericspatialdao.exceptions.DAOException;

/**
 * 
 * @author Joao Savio C. Longo - joaosavio@gmail.com
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
		LOG.info("Getting entity manager factory for persistence unit: "
				+ persistenceUnitName);
		if (factories.containsKey(persistenceUnitName)) {
			LOG.info("Entity manager factor unit cached. Returning it");
			return factories.get(persistenceUnitName);
		}
		EntityManagerFactory emf;
		try {
			LOG.info("Creating a new entity manager factory");
			emf = Persistence.createEntityManagerFactory(persistenceUnitName);
		} catch (Exception e) {
			String message = FAILED_TO_LOAD_PERSISTENCE_UNIT + e.getMessage();
			if (e.getCause() != null) {
				message = message + ". Cause: " + e.getCause().getMessage();
			}
			LOG.error(message);
			throw new DAOException(message);
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
