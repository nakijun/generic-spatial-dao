package org.genericspatialdao.utils;

import org.genericspatialdao.dao.DAO;

public class DAOUtils {

	@SuppressWarnings("rawtypes")
	public static void close(DAO... dao) {
		for (DAO d : dao) {
			d.close();
		}
	}
}
