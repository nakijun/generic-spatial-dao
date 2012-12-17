package org.genericspatialdao.service;

import static org.junit.Assert.assertNotNull;

import org.genericspatialdao.configuration.DaoConfiguration;
import org.genericspatialdao.util.TestUtils;
import org.genericspatialdao.util.TestUtils.DB;
import org.junit.Test;

public class EntityManagerServiceTest {

	@Test
	public void getEntityManagerTest() {
		assertNotNull(EntityManagerService
				.getEntityManager(new DaoConfiguration(DB.DB_1.toString(),
						TestUtils.buildPropertiesMap(DB.DB_1))));
	}

	@Test
	public void closeQuietlyTest() {
		EntityManagerService.closeQuietly(new DaoConfiguration("notExistPU"));
	}
}