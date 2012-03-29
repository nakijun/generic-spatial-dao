package org.genericspatialdao.dao;

import org.apache.log4j.Logger;
import org.genericspatialdao.utils.ConstantsUtils;

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
		return getDAO(entityClass, ConstantsUtils.DEFAULT_PERSISTENCE_UNIT,
				ConstantsUtils.DEFAULT_AUTO_TRANSACTION);
	}

	/**
	 * 
	 * @param entityClass
	 * @param persistenceUnitName
	 * @return a DAO implementation
	 */
	public static <T> DAO<T> getDAO(Class<T> entityClass,
			String persistenceUnitName, boolean autoTransaction) {
		LOG.info(CREATING_DAO_FOR_CLASS + entityClass.getName()
				+ " using persistence unit: " + persistenceUnitName
				+ " . Auto transaction: " + autoTransaction);
		return new GenericSpatialDAO<T>(entityClass, persistenceUnitName,
				autoTransaction);
	}

	public static void close(DAO<?>... dao) {
		LOG.info("Closing sessions");
		for (DAO<?> d : dao) {
			d.close();
		}
	}
}
