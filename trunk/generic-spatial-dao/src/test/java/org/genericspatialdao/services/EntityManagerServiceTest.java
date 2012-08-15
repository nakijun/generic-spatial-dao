package org.genericspatialdao.services;

import static org.junit.Assert.assertNotNull;

import org.genericspatialdao.service.EntityManagerService;
import org.genericspatialdao.util.ConstantUtils;
import org.junit.Test;

public class EntityManagerServiceTest {

	@Test
	public void getEntityManagerTest() {
		assertNotNull(EntityManagerService
				.getEntityManager(ConstantUtils.DEFAULT_PERSISTENCE_UNIT));
	}
}
