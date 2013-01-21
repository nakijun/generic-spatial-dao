package org.genericspatialdao.service;

import static org.junit.Assert.assertNotNull;

import org.genericspatialdao.configuration.DaoConfiguration;
import org.genericspatialdao.util.TestUtils;
import org.genericspatialdao.util.TestUtils.Database;
import org.junit.Test;

public class EntityManagerServiceTest {

	@Test
	public void getEntityManagerTest() {
		assertNotNull(EntityManagerService
				.getEntityManager(new DaoConfiguration(Database.DB_1.toString(),
						TestUtils.buildPropertiesMap(Database.DB_1))));
	}

	@Test
	public void closeQuietlyTest() {
		EntityManagerService.closeQuietly(new DaoConfiguration("notExistPU"));
	}
}