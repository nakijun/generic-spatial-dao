package org.genericspatialdao.dao;

import static org.junit.Assert.assertEquals;

import org.genericspatialdao.data.TestVO;
import org.junit.Test;

public class DAOFactoryTest {

	@Test
	public void test() {
		DAO<TestVO> testDAO = DAOFactory.getDAO(TestVO.class);
		assertEquals(true, testDAO instanceof DAO);
	}

}
