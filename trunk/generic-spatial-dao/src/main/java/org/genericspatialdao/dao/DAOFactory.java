package org.genericspatialdao.dao;

import org.apache.log4j.Logger;

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
		LOG.info("Creating DAO for class " + clazz.getName());
		return new GenericSpatialDAO<T>(clazz);
	}

	public static void close(DAO<?>... dao) {
		LOG.info("Closing sessions");
		for (DAO<?> d : dao) {
			d.close();
		}
	}
}
