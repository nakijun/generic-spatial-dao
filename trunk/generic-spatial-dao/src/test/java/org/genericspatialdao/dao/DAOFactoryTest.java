package org.genericspatialdao.dao;

import static org.junit.Assert.assertEquals;

import org.genericspatialdao.configuration.DAOConfiguration;
import org.genericspatialdao.dao.impl.DAOFactory;
import org.genericspatialdao.data.SpatialTestVO;
import org.genericspatialdao.data.TestVO;
import org.genericspatialdao.util.TestUtils;
import org.genericspatialdao.util.TestUtils.DB;
import org.junit.Test;

public class DAOFactoryTest {

	@Test
	public void test() {
		DAO<TestVO> testDAO = DAOFactory.getDAO(
				TestVO.class,
				new DAOConfiguration(DB.DB_1.toString(), TestUtils
						.buildPropertiesMap(DB.DB_1)));
		assertEquals(true, testDAO instanceof DAO);
	}

	@Test
	public void closeTest() {
		DAO<TestVO> testDAO = DAOFactory.getDAO(
				TestVO.class,
				new DAOConfiguration(DB.DB_1.toString(), TestUtils
						.buildPropertiesMap(DB.DB_1)));
		DAO<SpatialTestVO> testDAO2 = DAOFactory.getDAO(
				SpatialTestVO.class,
				new DAOConfiguration(DB.DB_1.toString(), TestUtils
						.buildPropertiesMap(DB.DB_1)));
		DAOFactory.close(testDAO, testDAO2);
	}

	@Test
	public void closeAllTest() {
		DAOFactory.closeAll();
	}
}
