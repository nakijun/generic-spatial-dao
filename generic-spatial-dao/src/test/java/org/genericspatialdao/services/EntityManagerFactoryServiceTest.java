package org.genericspatialdao.services;

import org.genericspatialdao.dao.DAOFactory;
import org.genericspatialdao.exceptions.DAOException;
import org.junit.Test;

public class EntityManagerFactoryServiceTest {

	@Test
	public void restartingFactoriesTest() {
		DAOFactory.closeAll();
		EntityManagerFactoryService.closeFactories();
		EntityManagerFactoryService.getEntityManagerFactory("default");
		EntityManagerFactoryService
				.getEntityManagerFactory("genericspatialdao2");
		EntityManagerFactoryService
				.getEntityManagerFactory("genericspatialdao3");
		EntityManagerFactoryService
				.getEntityManagerFactory("genericspatialdao4");
		DAOFactory.closeAll();
	}
	
	@Test(expected = DAOException.class)
	public void wrongTest() {
		EntityManagerFactoryService.getEntityManagerFactory("notExistsPU");
	}
}
