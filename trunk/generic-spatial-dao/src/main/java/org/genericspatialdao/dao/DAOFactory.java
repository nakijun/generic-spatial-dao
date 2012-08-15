package org.genericspatialdao.dao;

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
	 * @param persistenceUnitName
	 * @return a DAO implementation
	 */
	public static <T> DAO<T> getDAO(Class<T> entityClass,
			String persistenceUnitName) {
		return getDAO(entityClass, persistenceUnitName,
				ConstantUtils.DEFAULT_AUTO_TRANSACTION);
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
