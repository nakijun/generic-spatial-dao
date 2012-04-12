package org.genericspatialdao.services;

import org.junit.Test;

public class EntityManagerFactoryServiceTest {

	@Test
	public void test() {
		EntityManagerFactoryService.closeFactories();
		EntityManagerFactoryService.getEntityManagerFactory("default");
		EntityManagerFactoryService
				.getEntityManagerFactory("genericspatialdao2");
		EntityManagerFactoryService
				.getEntityManagerFactory("genericspatialdao3");
		EntityManagerFactoryService
				.getEntityManagerFactory("genericspatialdao4");
		EntityManagerFactoryService.closeFactories();
		EntityManagerFactoryService.getEntityManagerFactory("default");
	}
}
