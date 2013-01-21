package org.genericspatialdao.dao;

import static org.junit.Assert.assertEquals;

import org.genericspatialdao.configuration.DaoConfiguration;
import org.genericspatialdao.dao.impl.DaoFactory;
import org.genericspatialdao.data.SpatialTestVO;
import org.genericspatialdao.data.TestVO;
import org.genericspatialdao.util.TestUtils;
import org.genericspatialdao.util.TestUtils.Database;
import org.junit.Test;

public class DaoFactoryTest {

	@Test
	public void test() {
		Dao<TestVO> testDAO = DaoFactory.getDAO(
				TestVO.class,
				new DaoConfiguration(Database.DB_1.toString(), TestUtils
						.buildPropertiesMap(Database.DB_1)));
		assertEquals(true, testDAO instanceof Dao);
	}

	@Test
	public void closeTest() {
		Dao<TestVO> testDAO = DaoFactory.getDAO(
				TestVO.class,
				new DaoConfiguration(Database.DB_1.toString(), TestUtils
						.buildPropertiesMap(Database.DB_1)));
		Dao<SpatialTestVO> testDAO2 = DaoFactory.getDAO(
				SpatialTestVO.class,
				new DaoConfiguration(Database.DB_1.toString(), TestUtils
						.buildPropertiesMap(Database.DB_1)));
		DaoFactory.close(testDAO, testDAO2);
	}

	@Test
	public void closeAllTest() {
		DaoFactory.closeAll();
	}
}
