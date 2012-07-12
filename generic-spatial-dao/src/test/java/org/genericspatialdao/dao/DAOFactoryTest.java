package org.genericspatialdao.dao;

import static org.junit.Assert.assertEquals;

import org.genericspatialdao.data.SpatialTestVO;
import org.genericspatialdao.data.TestVO;
import org.junit.Test;

public class DAOFactoryTest {

	@Test
	public void test() {
		DAO<TestVO> testDAO = DAOFactory.getDAO(TestVO.class);
		assertEquals(true, testDAO instanceof DAO);
	}

	@Test
	public void closeTest() {
		DAO<TestVO> testDAO = DAOFactory.getDAO(TestVO.class);
		DAO<SpatialTestVO> testDAO2 = DAOFactory.getDAO(SpatialTestVO.class);
		DAOFactory.close(testDAO, testDAO2);
	}

	@Test
	public void closeAllTest() {
		DAOFactory.closeAll();
	}
}
