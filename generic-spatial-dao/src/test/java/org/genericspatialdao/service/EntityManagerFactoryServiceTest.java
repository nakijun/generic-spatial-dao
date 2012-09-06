package org.genericspatialdao.service;

import org.genericspatialdao.configuration.DAOConfiguration;
import org.genericspatialdao.dao.impl.DAOFactory;
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
		EntityManagerFactoryService
				.getEntityManagerFactory(new DAOConfiguration(DB.DB_1
						.toString(), TestUtils.buildPropertiesMap(DB.DB_1)));
		EntityManagerFactoryService
				.getEntityManagerFactory(new DAOConfiguration(DB.DB_2
						.toString(), TestUtils.buildPropertiesMap(DB.DB_2)));
		EntityManagerFactoryService
				.getEntityManagerFactory(new DAOConfiguration(DB.DB_3
						.toString(), TestUtils.buildPropertiesMap(DB.DB_3)));
		EntityManagerFactoryService
				.getEntityManagerFactory(new DAOConfiguration(DB.DB_4
						.toString(), TestUtils.buildPropertiesMap(DB.DB_4)));
		DAOFactory.closeAll();
	}

	@Test(expected = DAOException.class)
	public void wrongTest() {
		EntityManagerFactoryService
				.getEntityManagerFactory(new DAOConfiguration("notExistsPU"));
	}
}