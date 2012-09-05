package org.genericspatialdao.dao.impl;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.genericspatialdao.dao.DAO;
import org.genericspatialdao.dao.DAOFactory;
import org.genericspatialdao.data.TestGeneratorVO;
import org.genericspatialdao.util.TestUtils;
import org.genericspatialdao.util.TestUtils.DB;
import org.junit.Test;

public class GeneratorTest {

	@Test
	public void test() {
		DAO<TestGeneratorVO> testG = TestUtils.getDAOTest(
				TestGeneratorVO.class, DB.DB_1);
		List<TestGeneratorVO> list = new ArrayList<TestGeneratorVO>();
		for (int i = 0; i < 5; i++) {
			TestGeneratorVO tg = new TestGeneratorVO();
			tg.setName(TestUtils.randomString());
			list.add(tg);
		}
		testG.persist(list);

		for (TestGeneratorVO tg : list) {
			assertTrue(tg.getId() >= 100);
		}

		testG.remove(list);
		DAOFactory.close(testG);
	}
}
