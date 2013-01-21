package org.genericspatialdao.service;

import org.genericspatialdao.configuration.DaoConfiguration;
import org.genericspatialdao.dao.impl.DaoFactory;
import org.genericspatialdao.exception.DaoException;
import org.genericspatialdao.service.EntityManagerFactoryService;
import org.genericspatialdao.util.TestUtils;
import org.genericspatialdao.util.TestUtils.Database;
import org.junit.Test;

public class EntityManagerFactoryServiceTest {

	@Test
	public void restartingFactoriesTest() {
		DaoFactory.closeAll();
		EntityManagerFactoryService.closeFactories();
		EntityManagerFactoryService
				.getEntityManagerFactory(new DaoConfiguration(Database.DB_1
						.toString(), TestUtils.buildPropertiesMap(Database.DB_1)));
		EntityManagerFactoryService
				.getEntityManagerFactory(new DaoConfiguration(Database.DB_2
						.toString(), TestUtils.buildPropertiesMap(Database.DB_2)));
		EntityManagerFactoryService
				.getEntityManagerFactory(new DaoConfiguration(Database.DB_3
						.toString(), TestUtils.buildPropertiesMap(Database.DB_3)));
		EntityManagerFactoryService
				.getEntityManagerFactory(new DaoConfiguration(Database.DB_4
						.toString(), TestUtils.buildPropertiesMap(Database.DB_4)));
		DaoFactory.closeAll();
	}

	@Test(expected = DaoException.class)
	public void wrongTest() {
		EntityManagerFactoryService
				.getEntityManagerFactory(new DaoConfiguration("notExistsPU"));
	}
}