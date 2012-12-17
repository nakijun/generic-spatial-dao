package org.genericspatialdao.dao.impl;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.genericspatialdao.dao.Dao;
import org.genericspatialdao.data.TestVO;
import org.genericspatialdao.util.TestUtils;
import org.genericspatialdao.util.TestUtils.DB;
import org.junit.Test;

public class GenericSpatialDaoIT {

	public static final int TIMES = 100;
	public static final int THREADS = 10;
	private ExecutorService executor = Executors
			.newFixedThreadPool(THREADS * 4);

	@Test
	public void persistUpdateRemoveMultiThreadSamePUTest()
			throws InterruptedException {
		System.out.println("persistUpdateRemoveMultiThreadTest");
		for (int i = 0; i < THREADS; i++) {
			executor.execute(new MultiThreadDefaultPUTest());
		}
		executor.awaitTermination(5L, TimeUnit.SECONDS);
		executor.shutdown();
	}

	@Test
	public void persistUpdateRemoveMultiThreadDifferentPUTest()
			throws InterruptedException {
		System.out.println("persistUpdateRemoveMultiThreadDifferentPUTest");
		for (int i = 0; i < THREADS; i++) {
			executor.execute(new MultiThreadDefaultPUTest());
			executor.execute(new MultiThreadPU2Test());
			executor.execute(new MultiThreadPU3Test());
			executor.execute(new MultiThreadPU4Test());
		}
		executor.awaitTermination(8L, TimeUnit.SECONDS);
		executor.shutdown();
	}
}

abstract class MultiThreadTest implements Runnable {
	@Override
	public void run() {
		Dao<TestVO> testDAO = null;
		for (int i = 0; i < GenericSpatialDaoIT.TIMES; i++) {
			testDAO = getDAO();
			TestVO testVO = new TestVO();
			testVO.setLogin(TestUtils.randomString());
			testVO.setPassword(TestUtils.randomString());

			testDAO.persist(testVO);

			assertEquals(testVO, testDAO.find(testVO.getId()));

			String newPassword = TestUtils.randomString();
			testVO.setPassword(newPassword);
			testDAO.merge(testVO);

			TestVO updatedTestVO = testDAO.find(testVO.getId());
			assertEquals(newPassword, updatedTestVO.getPassword());

			testDAO.remove(updatedTestVO);
			assertEquals(null, testDAO.find(testVO.getId()));
		}
		testDAO.close();
	}

	abstract Dao<TestVO> getDAO();
}

class MultiThreadDefaultPUTest extends MultiThreadTest {

	@Override
	Dao<TestVO> getDAO() {
		return TestUtils.getDAOTest(TestVO.class, DB.DB_1);
	}
}

class MultiThreadPU2Test extends MultiThreadTest {

	@Override
	Dao<TestVO> getDAO() {
		return TestUtils.getDAOTest(TestVO.class, DB.DB_2);
	}
}

class MultiThreadPU3Test extends MultiThreadTest {

	@Override
	Dao<TestVO> getDAO() {
		return TestUtils.getDAOTest(TestVO.class, DB.DB_3);
	}
}

class MultiThreadPU4Test extends MultiThreadTest {

	@Override
	Dao<TestVO> getDAO() {
		return TestUtils.getDAOTest(TestVO.class, DB.DB_4);
	}
}

class MultiThreadDifferentPUTest implements Runnable {

	@Override
	public void run() {
		Dao<TestVO> testDAO1 = TestUtils.getDAOTest(TestVO.class, DB.DB_1);
		Dao<TestVO> testDAO2 = TestUtils.getDAOTest(TestVO.class, DB.DB_2);
		doTest(testDAO1);
		doTest(testDAO2);

		testDAO1.close();
		testDAO2.close();
	}

	public void doTest(Dao<TestVO> testDAO) {
		for (int i = 0; i < GenericSpatialDaoIT.TIMES; i++) {
			TestVO testVO = new TestVO();
			testVO.setLogin(TestUtils.randomString());
			testVO.setPassword(TestUtils.randomString());

			testDAO.persist(testVO);

			assertEquals(testVO, testDAO.find(testVO.getId()));

			String newPassword = TestUtils.randomString();
			testVO.setPassword(newPassword);
			testDAO.merge(testVO);

			TestVO updatedTestVO = testDAO.find(testVO.getId());
			assertEquals(newPassword, updatedTestVO.getPassword());

			testDAO.remove(updatedTestVO);
			assertEquals(null, testDAO.find(testVO.getId()));
		}
	}
}