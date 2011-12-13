package org.genericspatialdao.dao;

public class DAOFactory {

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> DAO<T> getDAO(Class<T> clazz) {
		return new GenericSpatialDAO<T>(clazz);
	}
}
