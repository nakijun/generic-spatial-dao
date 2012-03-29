package org.genericspatialdao.dao;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.genericspatialdao.utils.PropertiesUtils;

public class DAOFactory {

	private static final Logger LOG = Logger.getLogger(DAOFactory.class);

	private DAOFactory() {

	}

	/**
	 * 
	 * @param clazz
	 * @return a DAO implementation
	 */
	public static <T> DAO<T> getDAO(Class<T> clazz) {
		String defaultPersistenceUnit = PropertiesUtils
				.getString("persistenceunit.default");
		if (StringUtils.isBlank(defaultPersistenceUnit)) {
			defaultPersistenceUnit = "default";
		}
		return getDAO(clazz, defaultPersistenceUnit);
	}

	/**
	 * 
	 * @param clazz
	 * @return a DAO implementation
	 */
	public static <T> DAO<T> getDAO(Class<T> clazz, String persistenceUnitName) {
		LOG.info("Creating DAO for class " + clazz.getName()
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
