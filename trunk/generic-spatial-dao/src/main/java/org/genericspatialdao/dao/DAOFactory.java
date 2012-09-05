package org.genericspatialdao.dao;

import org.apache.log4j.Logger;
import org.genericspatialdao.configuration.DAOConfiguration;
import org.genericspatialdao.dao.impl.GenericSpatialDAO;
import org.genericspatialdao.service.EntityManagerFactoryService;
import org.genericspatialdao.service.EntityManagerService;

/**
 * 
 * @author Joao Savio C. Longo - joaosavio@gmail.com
 * 
 */
public class DAOFactory {

	private static final String CREATING_DAO_FOR_CLASS = "Creating DAO for class ";
	private static final Logger LOG = Logger.getLogger(DAOFactory.class);

	private DAOFactory() {

	}

	/**
	 * 
	 * @param entityClass
	 * @param configuration
	 * @return a DAO implementation
	 */
	public static <T> DAO<T> getDAO(Class<T> entityClass,
			DAOConfiguration configuration) {
		if (LOG.isInfoEnabled()) {
			LOG.info(CREATING_DAO_FOR_CLASS + entityClass.getName()
					+ ". Persistence unit: "
					+ configuration.getPersistenceUnit() + ". Properties: "
					+ configuration.getProperties() + ". Auto transaction: "
					+ configuration.isAutoTransaction());
		}

		return new GenericSpatialDAO<T>(entityClass, configuration);
	}

	/**
	 * 
	 * @param dao
	 */
	public static void close(DAO<?>... dao) {
		LOG.info("Closing sessions");
		for (DAO<?> d : dao) {
			d.close();
		}
	}

	/**
	 * Close entity manager and all factories
	 */
	public static void closeAll() {
		LOG.info("Closing all");
		EntityManagerService.closeAll();
		EntityManagerFactoryService.closeFactories();
	}
}
