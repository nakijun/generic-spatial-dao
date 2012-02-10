package org.genericspatialdao.utils;

import org.apache.log4j.Logger;
import org.genericspatialdao.dao.DAO;

public class DAOUtils {
	
	private static final Logger LOG = Logger
			.getLogger(DAOUtils.class);

	public static void close(DAO<?>... dao) {
		LOG.info("Closing sessions");
		for (DAO<?> d : dao) {
			d.close();
		}
	}
}
