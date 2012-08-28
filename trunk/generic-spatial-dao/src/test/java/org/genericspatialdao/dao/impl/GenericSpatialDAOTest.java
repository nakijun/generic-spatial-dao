package org.genericspatialdao.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.genericspatialdao.dao.DAO;
import org.genericspatialdao.dao.DAOFactory;
import org.genericspatialdao.data.SpatialTestVO;
import org.genericspatialdao.data.TestVO;
import org.genericspatialdao.exception.DAOException;
import org.genericspatialdao.util.SpatialUtils;
import org.genericspatialdao.util.TestUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernatespatial.criterion.SpatialRestrictions;
import org.junit.Test;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class GenericSpatialDAOTest {

	private final int SRID = 4326;

	@Test
	public void removeAllTest() {
		System.out.println("removeAllTest");
		DAO<TestVO> testDAO = DAOFactory.getDAO(TestVO.class);

		TestVO testVO = new TestVO();
		testVO.setLogin(TestUtils.randomString());
		testVO.setPassword(TestUtils.randomString());

		testDAO.persist(testVO);
		assertEquals(1, testDAO.findAll().size());

		testDAO.removeAll();
		assertEquals(0, testDAO.findAll().size());

		testDAO.close();
	}

	@Test
	public void countTest() {
		System.out.println("countTest");
		DAO<TestVO> testDAO = DAOFactory.getDAO(TestVO.class);
		TestVO testVO = new TestVO();
		testVO.setLogin(TestUtils.randomString());
		testVO.setPassword(TestUtils.randomString());

		TestVO testVO2 = new TestVO();
		testVO2.setLogin(TestUtils.randomString());
		testVO2.setPassword(TestUtils.randomString());

		testDAO.persist(testVO, testVO2);
		assertEquals(2, testDAO.findAll().size());
		assertEquals(2L, testDAO.count());
		testDAO.removeAll();

		testDAO.close();
	}

	@Test(expected = DAOException.class)
	public void clearTest() {
		System.out.println("clearTest");
		DAO<TestVO> testDAO = DAOFactory.getDAO(TestVO.class);
		TestVO testVO = new TestVO();
		testVO.setLogin(TestUtils.randomString());
		testVO.setPassword(TestUtils.randomString());
		testDAO.persist(testVO);

		testDAO.clear();
		try {
			testDAO.remove(testVO);
		} finally {
			testDAO.removeAll();
			testDAO.close();
		}
	}

	@Test
	public void persistUpdateRemoveTest() {
		System.out.println("persistUpdateRemoveTest");
		DAO<TestVO> testDAO = DAOFactory.getDAO(TestVO.class);

		TestVO testVO = new TestVO();
		testVO.setLogin(TestUtils.randomString());
		testVO.setPassword(TestUtils.randomString());

		testDAO.persist(testVO);

		List<TestVO> result = testDAO.findAll();
		assertEquals(1, result.size());
		assertEquals(testVO, result.get(0));

		testVO = result.get(0);
		String newPassword = TestUtils.randomString();
		testVO.setPassword(newPassword);
		testDAO.merge(testVO);

		TestVO updatedTestVO = testDAO.find(testVO.getId());
		assertEquals(newPassword, updatedTestVO.getPassword());

		testDAO.remove(updatedTestVO);
		assertEquals(0, testDAO.findAll().size());

		testDAO.close();
	}

	@Test
	public void persistUpdateByFlushAndRemoveSpatialTest() {
		System.out.println("persistUpdateByFlushAndRemoveSpatialTest");
		DAO<SpatialTestVO> testDAO = DAOFactory.getDAO(SpatialTestVO.class);

		SpatialTestVO spatialTestVO = new SpatialTestVO();
		spatialTestVO.setPoint(TestUtils.randomLatLongPoint(SRID));

		testDAO.persist(spatialTestVO);

		List<SpatialTestVO> result = testDAO.findAll();
		assertEquals(1, result.size());
		assertEquals(spatialTestVO, result.get(0));

		spatialTestVO = result.get(0);
		Point newPoint = TestUtils.randomLatLongPoint(SRID);
		spatialTestVO.setPoint(newPoint);
		testDAO.flush();

		SpatialTestVO updatedSpatialTestVO = testDAO
				.find(spatialTestVO.getId());
		assertEquals(newPoint, updatedSpatialTestVO.getPoint());

		testDAO.remove(updatedSpatialTestVO);
		assertEquals(0, testDAO.findAll().size());

		testDAO.close();
	}

	@Test
	public void hqlTest() {
		System.out.println("hqlTest");
		DAO<TestVO> testDAO = DAOFactory.getDAO(TestVO.class);

		String login1 = TestUtils.randomString();

		TestVO testVO1 = new TestVO();
		testVO1.setLogin(login1);
		testVO1.setPassword(TestUtils.randomString());
		testDAO.persist(testVO1);
		assertEquals(login1, testDAO.find(testVO1.getId()).getLogin());

		TestVO testVO2 = new TestVO();
		testVO2.setLogin(TestUtils.randomString());
		testVO2.setPassword(TestUtils.randomString());
		testDAO.persist(testVO2);
		assertEquals(2, testDAO.findAll().size());

		assertEquals(login1, testVO1.getLogin());
		assertEquals(
				1,
				testDAO.executeHQL(
						"FROM TestVO t WHERE login = '" + login1 + "'").size());

		testDAO.remove(testVO1, testVO2);
		testDAO.close();
	}

	@Test
	public void sqlTest() {
		System.out.println("sqlTest");
		DAO<TestVO> testDAO = DAOFactory.getDAO(TestVO.class);

		String login1 = TestUtils.randomString();

		TestVO testVO1 = new TestVO();
		testVO1.setLogin(login1);
		testVO1.setPassword(TestUtils.randomString());
		testDAO.persist(testVO1);
		assertEquals(login1, testDAO.find(testVO1.getId()).getLogin());

		TestVO testVO2 = new TestVO();
		testVO2.setLogin(TestUtils.randomString());
		testVO2.setPassword(TestUtils.randomString());
		testDAO.persist(testVO2);
		assertEquals(2, testDAO.findAll().size());

		assertEquals(login1, testVO1.getLogin());
		assertEquals(
				1,
				testDAO.executeSQL(
						"SELECT * FROM TestVO WHERE login = '" + login1 + "'")
						.size());

		testDAO.remove(testVO1, testVO2);
		testDAO.close();
	}

	@Test
	public void sqlUpdateAndRefreshTest() {
		System.out.println("sqlUpdateTest");
		DAO<TestVO> testDAO = DAOFactory.getDAO(TestVO.class);

		String login = TestUtils.randomString();
		String newLogin = TestUtils.randomString();

		TestVO testVO1 = new TestVO();
		testVO1.setLogin(login);
		testVO1.setPassword(TestUtils.randomString());
		testDAO.persist(testVO1);
		assertEquals(login, testDAO.find(testVO1.getId()).getLogin());
		assertEquals(login, testVO1.getLogin());

		testDAO.executeSQLUpdate("UPDATE TestVO SET login = '" + newLogin + "'");
		testDAO.refresh(testVO1);
		assertEquals(newLogin, testVO1.getLogin());
		testDAO.remove(testVO1);
		testDAO.close();
	}

	@Test
	public void hql2Test() {
		System.out.println("hqlTest");
		DAO<TestVO> testDAO = DAOFactory.getDAO(TestVO.class);

		String login1 = TestUtils.randomString();

		TestVO testVO1 = new TestVO();
		testVO1.setLogin(login1);
		testVO1.setPassword(TestUtils.randomString());
		testDAO.persist(testVO1);
		assertEquals(login1, testDAO.find(testVO1.getId()).getLogin());

		TestVO testVO2 = new TestVO();
		testVO2.setLogin(TestUtils.randomString());
		testVO2.setPassword(TestUtils.randomString());
		testDAO.persist(testVO2);
		assertEquals(2, testDAO.findAll().size());

		assertEquals(login1, testVO1.getLogin());
		assertEquals(1,
				testDAO.executeHQL("FROM TestVO t WHERE login = ?", login1)
						.size());

		testDAO.remove(testVO1, testVO2);
		testDAO.close();
	}

	@Test
	public void findAllTest() {
		System.out.println("findAllTest");
		DAO<TestVO> testDAO = DAOFactory.getDAO(TestVO.class);
		assertEquals(0, testDAO.findAll().size());
		assertEquals(0, testDAO.findAll(1, 100).size());
		testDAO.close();
	}

	@Test
	public void findTest() {
		System.out.println("findTest");
		DAO<TestVO> testDAO = DAOFactory.getDAO(TestVO.class);
		TestVO testVO = new TestVO();
		String login = TestUtils.randomString();
		String password = TestUtils.randomString();
		testVO.setLogin(login);
		testVO.setPassword(password);
		testDAO.persist(testVO);

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("login", login);
		properties.put("password", password);
		assertEquals(testVO, testDAO.find(testVO.getId()));
		assertEquals(testVO, testDAO.find(testVO.getId(), properties));

		testDAO.removeAll();
		testDAO.close();
	}

	@Test
	public void findListTest() {
		System.out.println("findListTest");
		DAO<TestVO> testDAO = DAOFactory.getDAO(TestVO.class);
		TestVO testVO = new TestVO();
		testVO.setLogin(TestUtils.randomString());
		testVO.setPassword(TestUtils.randomString());

		TestVO testVO2 = new TestVO();
		testVO2.setLogin(TestUtils.randomString());
		testVO2.setPassword(TestUtils.randomString());

		List<TestVO> list = new ArrayList<TestVO>();
		list.add(testVO);
		list.add(testVO2);
		testDAO.persist(list);

		assertEquals(list, testDAO.find(testVO.getId(), testVO2.getId()));

		testDAO.remove(list);
		testDAO.close();
	}

	@Test(expected = DAOException.class)
	public void insertWrongTest() {
		System.out.println("insertWrongTest");
		DAO<TestVO> testDAO = DAOFactory.getDAO(TestVO.class);

		TestVO testVO = new TestVO();
		testVO.setId(TestUtils.randomInt());
		testVO.setLogin(TestUtils.randomString());
		testVO.setPassword(TestUtils.randomString());
		try {
			testDAO.persist(testVO);
		} finally {
			testDAO.close();
		}
	}

	@Test
	public void withinTest() {
		System.out.println("withinTest");
		final int NUM = 50;
		DAO<SpatialTestVO> testDAO = DAOFactory.getDAO(SpatialTestVO.class);
		List<SpatialTestVO> list = new ArrayList<SpatialTestVO>();
		for (int i = 0; i < NUM; i++) {
			SpatialTestVO spatialVO = new SpatialTestVO(TestUtils.randomPoint(
					-179, 179, -89, 89, SRID));
			list.add(spatialVO);
		}
		testDAO.persist(list);

		Polygon polygon = SpatialUtils
				.createPolygon(
						"POLYGON((-180 -90, -180 90, 180 90, 180 -90, -180 -90))",
						SRID);
		List<Criterion> conditions = new ArrayList<Criterion>();
		Criterion c1 = SpatialRestrictions.within("point", polygon);
		conditions.add(c1);

		assertEquals(NUM, testDAO.findByCriteria(conditions).size());
		assertEquals(NUM,
				testDAO.findByCriteria(conditions, Order.asc("id"), 0, 100)
						.size());

		testDAO.remove(list);

		testDAO.close();
	}

	@Test
	public void findAddMergeRemoveAndRefreshEmptyTest() {
		System.out.println("findAddMergeRemoveAndRefreshEmptyTest");
		DAO<SpatialTestVO> testDAO = DAOFactory.getDAO(SpatialTestVO.class);
		assertNull(testDAO.find());
		testDAO.persist(new ArrayList<SpatialTestVO>());
		testDAO.merge(new ArrayList<SpatialTestVO>());
		testDAO.remove(new ArrayList<SpatialTestVO>());
		testDAO.merge(new ArrayList<SpatialTestVO>());
	}

}
