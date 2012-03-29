package org.genericspatialdao.services;

import static org.junit.Assert.assertNotNull;

import org.genericspatialdao.utils.ConstantsUtils;
import org.junit.Test;

public class EntityManagerServiceTest {

	@Test
	public void getEntityManagerTest() {
		assertNotNull(EntityManagerService
				.getEntityManager(ConstantsUtils.DEFAULT_PERSISTENCE_UNIT));
	}
}
