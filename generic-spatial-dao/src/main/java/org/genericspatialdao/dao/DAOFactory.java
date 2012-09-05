package org.genericspatialdao.dao;

import java.util.Map;

import org.apache.log4j.Logger;
import org.genericspatialdao.dao.impl.GenericSpatialDAO;
import org.genericspatialdao.service.EntityManagerFactoryService;
import org.genericspatialdao.service.EntityManagerService;
import org.genericspatialdao.util.ConstantUtils;

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
	 * @return a DAO implementation
	 */
	public static <T> DAO<T> getDAO(Class<T> entityClass) {
		return getDAO(entityClass, ConstantUtils.DEFAULT_PERSISTENCE_UNIT);
	}

	/**
	 * 
	 * @param entityClass
	 * @param persistenceUnit
	 * @return a DAO implementation
	 */
	public static <T> DAO<T> getDAO(Class<T> entityClass, String persistenceUnit) {
		return getDAO(entityClass, persistenceUnit, null);
	}

	/**
	 * 
	 * @param entityClass
	 * @param persistenceUnit
	 * @param properties
	 * @return a DAO implementation
	 */
	public static <T> DAO<T> getDAO(Class<T> entityClass,
			String persistenceUnit, Map<String, String> properties) {
		return getDAO(entityClass, persistenceUnit, properties,
				ConstantUtils.DEFAULT_AUTO_TRANSACTION);
	}

	/**
	 * 
	 * @param entityClass
	 * @param persistenceUnit
	 * @param autoTransaction
	 * @return a DAO implementation
	 */
	public static <T> DAO<T> getDAO(Class<T> entityClass,
			String persistenceUnit, Map<String, String> properties,
			boolean autoTransaction) {
		if (LOG.isInfoEnabled()) {
			LOG.info(CREATING_DAO_FOR_CLASS + entityClass.getName()
					+ ". Persistence unit: " + persistenceUnit
					+ ". Properties: " + properties + ". Auto transaction: "
					+ autoTransaction);
		}

		return new GenericSpatialDAO<T>(entityClass, persistenceUnit,
				properties, autoTransaction);
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
