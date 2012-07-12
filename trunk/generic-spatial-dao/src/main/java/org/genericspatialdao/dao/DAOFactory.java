package org.genericspatialdao.dao;

import org.apache.log4j.Logger;
import org.genericspatialdao.services.EntityManagerFactoryService;
import org.genericspatialdao.services.EntityManagerService;
import org.genericspatialdao.utils.ConstantsUtils;

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
		return getDAO(entityClass, ConstantsUtils.DEFAULT_PERSISTENCE_UNIT);
	}

	/**
	 * 
	 * @param entityClass
	 * @param persistenceUnitName
	 * @return a DAO implementation
	 */
	public static <T> DAO<T> getDAO(Class<T> entityClass,
			String persistenceUnitName) {
		return getDAO(entityClass, persistenceUnitName,
				ConstantsUtils.DEFAULT_AUTO_TRANSACTION);
	}

	/**
	 * 
	 * @param entityClass
	 * @param persistenceUnitName
	 * @param autoTransaction
	 * @return a DAO implementation
	 */
	public static <T> DAO<T> getDAO(Class<T> entityClass,
			String persistenceUnitName, boolean autoTransaction) {
		LOG.info(CREATING_DAO_FOR_CLASS + entityClass.getName()
				+ ". Persistence unit: " + persistenceUnitName
				+ ". Auto transaction: " + autoTransaction);
		return new GenericSpatialDAO<T>(entityClass, persistenceUnitName,
				autoTransaction);
	}

	public static void close(DAO<?>... dao) {
		LOG.info("Closing sessions");
		for (DAO<?> d : dao) {
			d.close();
		}
	}

	public static void closeAll() {
		LOG.info("Closing all");
		EntityManagerService.close();
		EntityManagerFactoryService.closeFactories();
	}
}
