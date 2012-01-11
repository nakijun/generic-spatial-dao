package org.genericspatialdao.dao;

import static org.junit.Assert.assertTrue;

import org.genericspatialdao.data.TestGeneratorVO;
import org.genericspatialdao.utils.TestUtils;
import org.junit.Test;

public class GeneratorTest {

	@Test
	public void test() {
		DAO<TestGeneratorVO> testG = new GenericSpatialDAO<TestGeneratorVO>(
				TestGeneratorVO.class);
		for (int i = 0; i < 5; i++) {
			TestGeneratorVO tg = new TestGeneratorVO();
			tg.setName(TestUtils.randomString());
			testG.persist(tg);
			assertTrue(tg.getId() >= 100);
		}
	}
}
