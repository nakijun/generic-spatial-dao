package org.genericspatialdao.service;

import org.genericspatialdao.dao.DAOFactory;
import org.genericspatialdao.exception.DAOException;
import org.genericspatialdao.service.EntityManagerFactoryService;
import org.genericspatialdao.util.TestUtils;
import org.genericspatialdao.util.TestUtils.DB;
import org.junit.Test;

public class EntityManagerFactoryServiceTest {

	@Test
	public void restartingFactoriesTest() {
		DAOFactory.closeAll();
		EntityManagerFactoryService.closeFactories();
		EntityManagerFactoryService.getEntityManagerFactory(DB.DB_1.toString(),
				TestUtils.buildPropertiesMap(DB.DB_1));
		EntityManagerFactoryService.getEntityManagerFactory(DB.DB_2.toString(),
				TestUtils.buildPropertiesMap(DB.DB_2));
		EntityManagerFactoryService.getEntityManagerFactory(DB.DB_3.toString(),
				TestUtils.buildPropertiesMap(DB.DB_3));
		EntityManagerFactoryService.getEntityManagerFactory(DB.DB_4.toString(),
				TestUtils.buildPropertiesMap(DB.DB_4));
		DAOFactory.closeAll();
	}

	@Test(expected = DAOException.class)
	public void wrongTest() {
		EntityManagerFactoryService.getEntityManagerFactory("notExistsPU");
	}
}
