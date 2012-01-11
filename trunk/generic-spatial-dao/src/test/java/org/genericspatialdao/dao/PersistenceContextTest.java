package org.genericspatialdao.dao;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.genericspatialdao.exception.DAOException;
import org.junit.Test;

public class PersistenceContextTest {

	@Test
	public void loadAndUnloadPersistenceUnitsTest() {
		System.out.println("loadAndUnloadPersistenceUnitsTest");
		PersistenceContext.loadPersistenceUnit("default");
		PersistenceContext.loadPersistenceUnit("test");
		PersistenceContext.loadPersistenceUnit("default");
	}

	@Test
	public void defaultLoadOnInitTest() {
		assertTrue(PersistenceContext.isPersistenceUnitLoaded());
	}

	@Test
	public void closeFactoryTest() {
		System.out.println("closeFactoryTest");
		PersistenceContext.closeFactory();
		assertFalse(PersistenceContext.isPersistenceUnitLoaded());
		PersistenceContext.loadPersistenceUnit("default");
	}

	@Test
	public void closeEntityManagerTest() {
		System.out.println("closeEntityManagerTest");
		PersistenceContext.close();
		assertNotNull(PersistenceContext.getEntityManager());
	}

	@Test(expected = DAOException.class)
	public void getEntityManagerSessionAndFactoryClosedTest() {
		System.out.println("getEntityManagerSessionAndFactoryClosedTest");
		try {
			PersistenceContext.close();
			PersistenceContext.closeFactory();
			PersistenceContext.getEntityManager();
		} finally {
			PersistenceContext.loadPersistenceUnit("default");
		}
	}

}
