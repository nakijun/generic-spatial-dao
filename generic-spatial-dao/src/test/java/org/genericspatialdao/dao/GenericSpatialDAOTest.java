package org.genericspatialdao.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.genericspatialdao.data.SpatialTestVO;
import org.genericspatialdao.data.TestVO;
import org.genericspatialdao.exception.DAOException;
import org.genericspatialdao.utils.SpatialUtils;
import org.genericspatialdao.utils.TestUtils;
import org.hibernate.criterion.Criterion;
import org.hibernatespatial.criterion.SpatialRestrictions;
import org.junit.Test;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class GenericSpatialDAOTest {

	private final int SRID = 4326;

	@Test
	public void removeAllTest() {
		DAO<TestVO> testDAO = new GenericSpatialDAO<TestVO>(TestVO.class);
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
		DAO<TestVO> testDAO = new GenericSpatialDAO<TestVO>(TestVO.class);
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

	@Test
	public void persistUpdateRemoveTest() {
		System.out.println("persistUpdateRemoveTest");
		DAO<TestVO> testDAO = new GenericSpatialDAO<TestVO>(TestVO.class);

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
	public void persistUpdateRemoveSpatialTest() {
		System.out.println("persistUpdateRemoveSpatialTest");
		DAO<SpatialTestVO> testDAO = new GenericSpatialDAO<SpatialTestVO>(
				SpatialTestVO.class);

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
		DAO<TestVO> testDAO = new GenericSpatialDAO<TestVO>(TestVO.class);

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
	public void hql2Test() {
		System.out.println("hqlTest");
		DAO<TestVO> testDAO = new GenericSpatialDAO<TestVO>(TestVO.class);

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
	public void findTest() {
		System.out.println("findTest");
		DAO<TestVO> testDAO = new GenericSpatialDAO<TestVO>(TestVO.class);

		assertEquals(0, testDAO.findAll().size());

		testDAO.close();
	}

	@Test(expected = DAOException.class)
	public void insertWrongTest() {
		System.out.println("insertWrongTest");
		DAO<TestVO> testDAO = new GenericSpatialDAO<TestVO>(TestVO.class);

		TestVO testVO = new TestVO();
		testVO.setId(TestUtils.randomInt());
		testVO.setLogin(TestUtils.randomString());
		testVO.setPassword(TestUtils.randomString());

		testDAO.persist(testVO);

		testDAO.close();
	}

	@Test
	public void withinTest() {
		System.out.println("withinTest");
		final int NUM = 50;
		DAO<SpatialTestVO> testDAO = new GenericSpatialDAO<SpatialTestVO>(
				SpatialTestVO.class);
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

		testDAO.remove(list);

		testDAO.close();
	}

	@Test
	public void addUpdateAndRemoveEmptyTest() {
		System.out.println("removeNullTest");
		DAO<SpatialTestVO> testDAO = new GenericSpatialDAO<SpatialTestVO>(
				SpatialTestVO.class);
		testDAO.persist(new ArrayList<SpatialTestVO>());
		testDAO.merge(new ArrayList<SpatialTestVO>());
		testDAO.remove(new ArrayList<SpatialTestVO>());
	}

}
