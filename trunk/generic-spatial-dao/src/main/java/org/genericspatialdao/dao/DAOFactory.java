package org.genericspatialdao.dao;

import org.apache.log4j.Logger;

public class DAOFactory {

	private static final String CREATING_DAO_FOR_CLASS = "Creating DAO for class ";
	private static final Logger LOG = Logger.getLogger(DAOFactory.class);

	private DAOFactory() {

	}

	/**
	 * 
	 * @param clazz
	 * @return a DAO implementation
	 */
	public static <T> DAO<T> getDAO(Class<T> clazz) {
		LOG.info(CREATING_DAO_FOR_CLASS + clazz.getName()
				+ " using default persistence unit");
		return new GenericSpatialDAO<T>(clazz);
	}

	/**
	 * 
	 * @param clazz
	 * @return a DAO implementation
	 */
	public static <T> DAO<T> getDAO(Class<T> clazz, String persistenceUnitName) {
		LOG.info(CREATING_DAO_FOR_CLASS + clazz.getName()
				+ " using persistence unit: " + persistenceUnitName);
		return new GenericSpatialDAO<T>(clazz, persistenceUnitName);
	}

	public static void close(DAO<?>... dao) {
		LOG.info("Closing sessions");
		for (DAO<?> d : dao) {
			d.close();
		}
	}
}
