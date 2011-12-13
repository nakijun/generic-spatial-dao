package org.genericspatialdao.utils;

import org.genericspatialdao.dao.DAO;
import org.genericspatialdao.dao.GenericSpatialDAO;
import org.genericspatialdao.data.SpatialTestVO;
import org.genericspatialdao.data.TestVO;
import org.junit.Test;

public class DAOUtilsTest {

	@Test
	public void closeTest() {
		DAO<TestVO> testDAO = new GenericSpatialDAO<TestVO>(TestVO.class);
		DAO<SpatialTestVO> testDAO2 = new GenericSpatialDAO<SpatialTestVO>(
				SpatialTestVO.class);
		DAOUtils.close(testDAO, testDAO2);
	}

}
