package org.genericspatialdao.dao;

import org.junit.Test;

public class PersistenceContextTest {

	@Test
	public void simpleTest() {
		PersistenceContext.loadPersistenceUnit("default");
		PersistenceContext.loadPersistenceUnit("test");
		PersistenceContext.loadPersistenceUnit("default");
	}

}
