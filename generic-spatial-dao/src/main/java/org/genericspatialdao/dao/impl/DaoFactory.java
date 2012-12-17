package org.genericspatialdao.dao.impl;

import org.apache.log4j.Logger;
import org.genericspatialdao.configuration.DaoConfiguration;
import org.genericspatialdao.dao.Dao;
import org.genericspatialdao.service.EntityManagerFactoryService;
import org.genericspatialdao.service.EntityManagerService;

/**
 * 
 * @author Joao Savio C. Longo - joaosavio@gmail.com
 * 
 */
public final class DaoFactory {

	private static final String CREATING_DAO_FOR_CLASS = "Creating DAO for class: ";
	private static final Logger LOG = Logger.getLogger(DaoFactory.class);

	private DaoFactory() {

	}

	/**
	 * 
	 * @param entityClass
	 * @param configuration
	 * @return a DAO implementation
	 */
	public static <T> Dao<T> getDAO(Class<T> entityClass,
			DaoConfiguration configuration) {
		if (LOG.isInfoEnabled()) {
			LOG.info(CREATING_DAO_FOR_CLASS + entityClass.getName()
					+ ". Persistence unit: "
					+ configuration.getPersistenceUnit() + ". Properties: "
					+ configuration.getProperties() + ". Auto transaction: "
					+ configuration.isAutoTransaction());
		}

		return new GenericSpatialDao<T>(entityClass, configuration);
	}

	/**
	 * 
	 * @param dao
	 */
	public static void close(Dao<?>... dao) {
		LOG.info("Closing sessions");
		for (Dao<?> d : dao) {
			d.close();
		}
	}

	/**
	 * Close entity managers and all factories
	 */
	public static void closeAll() {
		LOG.info("Closing all");
		EntityManagerService.closeAll();
		EntityManagerFactoryService.closeFactories();
	}
}